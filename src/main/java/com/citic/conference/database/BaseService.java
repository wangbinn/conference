package com.citic.conference.database;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class BaseService {

	private Logger log = Logger.getLogger(this.getClass());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 获取数据库内的存储过程--返回一个ProcedureContext
	 * @param procedureName  存储过程名
	 * @param pm
	 * @return
	 */
	/**
	 * @SuppressWarnings()告诉编译器忽略指定的警告，不用在编译完成后出现警告信息。
	 * @SuppressWarnings("unchecked")告诉编译器忽略 unchecked 警告信息，
	 * 					如使用List，ArrayList等未进行参数化产生的警告信息。
	 *@SuppressWarnings(value={"unchecked", "rawtypes"})抑制多类型的警告
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ProcedureContext callProcedure(final String procedureName,
			final List<ProcedureParam> pm) {
		final List<ProcedureParam> inParams = new ArrayList();
		final List<ProcedureParam> outParams = new ArrayList();
		// 将输入in输出out参数分开
		if (pm.size() > 0) {
			for (ProcedureParam param : pm) {
				if ("IN".equals(param.getInOut().toUpperCase())) {
					inParams.add(param);
				} else if ("OUT".equals(param.getInOut().toUpperCase())) {
					outParams.add(param);
				}
			}
		}
		/**1、jdbcTemplate调用存储过程的步骤：
		 * 	（1)、通过Connection对象的prepareCall()方法创建一个CallableStatement对象的实例。
		 * 		在使用Connection对象的prepareCall()方法时，需要传入一个String类型的字符串，
		 * 		该字符串用于指明如何调用存储过程。
		 * 	(2)、通过CallableStatement对象的registerOutParameter()方法注册OUT参数；
		 * 			CallableStatement对象的setXxx()方法设定IN或IN OUT参数。
		 *  (3)、通过CallableStatement对象的execute()方法执行存储过程。
		 *  	如果所调用的是带返回参数的存储过程，还需要通过CallableStatement对象的getXxx()方法获取其返回值
		 *
		 *2、jdbcTemplate.execute(....)执行SQL语句
		 *  （1)、CallableStatementCreator.java接口中CallableStatement()方法，
		 * 		而CallableStatement对象为所有的DBMS提供了一种以标准形式调用已储存过程的方法
		 *   (2)、CallableStatementCallback.java接口用于在CallableStatement上运行的代码的通用回调接口。
		 * 		(允许在单个CallableStatement上执行任意数量的操作)*/
		ProcedureContext context = (ProcedureContext) jdbcTemplate.execute(
				new CallableStatementCreator() {
					//CallableStatement方法具有执行存储过程的方法，并可以设置存储过程的输入输出(in/out)参数
					public CallableStatement createCallableStatement(
							Connection con) throws SQLException {
						//inParams、outParams的尺寸是否为空，为空则inSize/outSize的值为0，不然为实际值
						int inSize = (inParams == null ? 0 : inParams.size());
						int outSize = (outParams == null ? 0 : outParams.size());
						StringBuffer sbsql = new StringBuffer();
						/**在 JDBC 中调用已储存过程的语法如下所示。注意，方括号-[]表示其内容是可选项且方括号本身并非语法的组成部份。
						 1、{call 过程名[(?, ?, ...)]}
						 2、返回结果参数的过程的语法为：{? = call 过程名[(?, ?, ...)]}
						 3、不带参数的已储存过程的语法类似：{call 过程名}**/
						sbsql.append("{call " + procedureName).append("(");
						for (int i = 0; i < (inSize + outSize); i++) {
							if (i == 0) {
								sbsql.append("?");
							} else {
								sbsql.append(",?");
							}
						}
						sbsql.append(")}");
						/**CallableStatement中定义的所有方法都用于处理 存储过程中方法的in/out参数，
						 * 	而CallableStatement 对象是用 Connection 方法 prepareCall 创建的。
						 * CallableStatement cstmt = con.prepareCall("{call getTestData(?, ?)}");
						 * 	其中?占位符为IN、OUT，取决于已储存过程getTestData。*/
						CallableStatement cs = con.prepareCall(sbsql.toString());

						// 设置输入参数的值
						if (inSize > 0) {
							for (int i = 0; i < inSize; i++) {
								ProcedureParam param = inParams.get(i);
								cs.setObject(param.getIndex(), param.getValue());
							}
						}
						// 注册输出参数的类型
						for (int i = 0; i < outSize; i++) {
							ProcedureParam param = outParams.get(i);
							cs.registerOutParameter(param.getIndex(),param.getParamType());
						}
						return cs;
					}
				}, new CallableStatementCallback() {
					public ProcedureContext doInCallableStatement(
							CallableStatement cs) throws SQLException,
                            DataAccessException {

						ProcedureContext pro = new ProcedureContext();
						JSONArray array = new JSONArray();
						//CallableStatement对象的execute()方法执行存储过程。
						boolean hashResult = cs.execute();
						while (true) {
							// 判断本次循环是否为数据集
							if (hashResult) {
								ResultSet rs = cs.getResultSet();
								// 获取列数；rs.getMetaData()得到结果集的结构，如获取列数、字段名等
								ResultSetMetaData metaData = rs.getMetaData();
								// json数组
								JSONArray innerResult = new JSONArray();
								while (rs.next()) {
									/**JSONObject：生成和解析json数据：
									 * 直接构建即直接实例化一个JSONObject对象，而后调用其put()方法，将数据写入。
									 * 	put()方法的第一个参数为key值，必须为String类型；第二个参数为value，
									 * 		可以为boolean、double、int、long、Object、Map以及Collection等*/
									// 遍历每一列
									JSONObject jsonObj = new JSONObject();
									for (int i = 1; i <= metaData.getColumnCount(); i++) {
										String columnName = metaData.getColumnLabel(i);
										jsonObj.put(columnName,rs.getObject(columnName));
									}
									innerResult.add(jsonObj);
								}
								array.add(innerResult);
							} else {
								int updateCount = cs.getUpdateCount();
								if (updateCount == -1)
									break;
							}
							/* 每次判断下一个是否为了数据集 */
							/* stmt.getMoreResults() 为 true表示下一次循环为数据集，false为空 */
							hashResult = cs.getMoreResults();
						}
						// 注册输出参数的类型
						for (int i = 0; i < outParams.size(); i++) {
							ProcedureParam param = outParams.get(i);
							param.setValue(cs.getString(param.getIndex()));
						}
						//ProcedureContext.class中定义的属性——params、datas、columns、otherDatas
						pro.setParams(pm);
						if (array.size() == 1) {
							pro.setDatas((JSONArray) array.get(0));
						} else if (array.size() == 2) {
							pro.setColumns((JSONArray) array.get(0));
							pro.setDatas((JSONArray) array.get(1));
						} else if (array.size() > 2) {
							pro.setColumns((JSONArray) array.get(0));
							pro.setDatas((JSONArray) array.get(1));
							List<JSONArray> otherDatas = new ArrayList<JSONArray>();
							for (int i = 2; i < array.size(); i++) {
								otherDatas.add((JSONArray) array.get(i));
							}
							pro.setOtherDatas(otherDatas);
							log.info("存储过程【"+procedureName+"】返回多个数据集，请注意数据的使用顺序！");
						}
						return pro;
					}
				});
		return context;
	}

	
	/** 
	* @Title: callProcedureWithOutParams 
	* @Description: 调用无参数的存储过程
	* @param @param procedureName
	* @return ProcedureContext
	* @throws 
	*/
	public ProcedureContext callProcedureWithOutParams(String procedureName) {
		List<ProcedureParam> params = new ArrayList<ProcedureParam>();
		return this.callProcedure(procedureName, params);
	}
	
	
	@Transactional
	public int[] batchUpdate(List<String> sqls){
		if(sqls.size() > 0){
			return jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
		}else {
			return new int[0];
		}
	}
	@Transactional
	public int[] batchUpdate(Vector<String> sqls){
		if(sqls.size() > 0){
			return jdbcTemplate.batchUpdate(sqls.toArray(new String[sqls.size()]));
		}else {
			return new int[0];
		}
	}
}
