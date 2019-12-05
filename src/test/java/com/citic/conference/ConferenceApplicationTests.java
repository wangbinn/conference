package com.citic.conference;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ConferenceApplicationTests {

    @Autowired
    BaseService baseService;

    @Autowired
    JdbcTemplate jdbcTemplate;
//    @Autowired
//    ProcedureParam procedureParam;

    @Test
    void contextLoads() {

        //judgeMeetingRoomTime("2019-12-01 14:00:00","2019-12-01 18:00:00");
        //Boolean user = registerUser("小宋", "123", "数字");
       // Boolean aBoolean = deleteRoomById(4);
       // System.out.println(aBoolean);
    }

    //用户登录
    public void dengLu(){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,"小0", Types.VARCHAR, "IN");
        ProcedureParam pp2 = new ProcedureParam(2,"123",Types.VARCHAR, "IN");
        pm.add(pp1);
        pm.add(pp2);
        ProcedureContext user_login = baseService.callProcedure("User_Login", pm);
        JSONArray datas = user_login.getDatas();
        for (int i=0;i<datas.size();i++){
            Map o = (Map)datas.get(i);
            //System.out.println(o.get(""));
            if (o.get("").equals("1")){
                System.out.println("用户存在");
                break;
            }
            if ((!o.get("").equals("-1"))&(!o.get("").equals("1"))){
                System.out.println("用户密码错误，密码提示为："+o.get(""));
                break;
            }
            if(i==datas.size()-1){
                System.out.println("用户未注册");
            }
        }
    }

    //根据用户姓名，将用户的会议室预订记录查询出来
    public void selectBookingRecord(){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,"小明", Types.VARCHAR, "IN");
        pm.add(pp1);
        ProcedureContext scheduledRecord =
                baseService.callProcedure("Select_ScheduledRecord", pm);
        JSONArray datas = scheduledRecord.getDatas();
        System.out.println(datas);

    }

    //根据前端传来的预订开始时间，和结束时间，将满足条件的会议室刷选出来
    public void judgeMeetingRoomTime(String startTime,String endTime){

        //impleDateFormat放便将转化时间
        SimpleDateFormat simdate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //输入开始时间与结束时间的初始值为当前时间值
        long init_time = new Date().getTime();
        long startTime_r=init_time,endTime_r=init_time;
        try {//将页面传入的字符串时间转为long型时间
            startTime_r=simdate.parse(startTime).getTime();
            endTime_r=simdate.parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,"2019-12-01 14:00:00", Types.VARCHAR, "IN");
        pm.add(pp1);
        ProcedureContext bookTimeSelect =
                baseService.callProcedure("BookTime_Select",pm);
        JSONArray datas = bookTimeSelect.getDatas();
       // System.out.println(datas);
        //若果返回集合大小为0，则用户预订时间所有会议室都满足
        if (datas.size()==0){
            //将所有会议室信息返回
            ProcedureContext book_all =
                    baseService.callProcedureWithOutParams("Book_Initialization");
            JSONArray book_allDatas = book_all.getDatas();
            System.out.println(book_allDatas);

        }else{
            //存放满足条件的会议室的信息
            JSONArray satisfydata=new JSONArray();
            //存储冲突的ID
            List conflictID=new ArrayList<>();
            for (int i=0;i<datas.size();i++){
                Map map=(Map)datas.get(i);
                //System.out.println(map);
                //获取预定记录中开始时间与结束时间
                String start_s = (String)map.get("scheduledStartTime");
                String end_s=(String)map.get("scheduledEndTime");
                try {
                    //将字符时间转为Long型时间
                    long startTime_s = simdate.parse(start_s).getTime();
                    long endTime_s = simdate.parse(end_s).getTime();
                    //System.out.println("getTime()"+startTime_s);
                    //在时间轴上，只有当输入时间结束时间在已预订开始时间与预订开始时间之间或
                    // 输入时间开始时间在已预订开始时间与预订开始时间之间；此时该会议室不能用
                    if (((startTime_s <startTime_r)&&(startTime_r< endTime_s))||
                            ((startTime_s<endTime_r)&&(endTime_r<endTime_s))){
                        //获取时间冲突会议室ID。一条冲突该会议室就不能用
                        int roomId=(int) map.get("meetingRoomId");
                        conflictID.add(roomId);
                       // System.out.println("冲突的会议室ID："+roomId);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            //查出所有会议ID，与冲突会议ID比较得出可预订房间
            List<Map<String, Object>> queryID =
                    jdbcTemplate.queryForList("SELECT DISTINCT(id) FROM MeetingRoom");
            for (int h=0;h<queryID.size();h++){
                int id=(int)queryID.get(h).get("id");
                boolean contains = conflictID.contains(id);
                if (contains==false){
                    //将不冲突的会议室ID传入，调用存储过程
                    List<ProcedureParam> pm_id = new ArrayList<ProcedureParam>();
                    ProcedureParam pp_id = new ProcedureParam(1,String.valueOf(id), Types.VARCHAR, "IN");
                    pm_id.add(pp_id);
                    ProcedureContext bookIdSelect = baseService.callProcedure("Book_Id_Select", pm_id);
                    JSONArray data1 = bookIdSelect.getDatas();
                    //将满足条件的会议信息添加到satisfydata中
                    satisfydata.add(data1.get(0));
                    //System.out.println(data1);
                   // satisfyID.add(id);
                }
            }
            //System.out.println(conflictID);
            //System.out.println(satisfyID);
            //System.out.println("satisfydata:"+satisfydata);
        }


    }

    public Boolean registerUser(String name,String password,String passwordhint){
        //将新用户信息注册到用户表
        String sql="INSERT INTO [User] (name,passWord,passWordHint) VALUES(?,?,?)";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql, name, password, passwordhint);
        }catch (Exception e){
             e.printStackTrace();
        }

        if (register==1){
            return true;
        }else {
            return false;
        }
    }

    public Boolean deleteRoomById(Integer id){
        String sql="DELETE FROM MeetingRoom WHERE id=?";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql, id);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }
}
