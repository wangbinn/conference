package com.citic.conference.service;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**管理者用户：
 * 1、管理者登录
 * 2、增加会议室
 * 3、删除会议室
 * 4、查询会议室
 */
@Service
public class PrincipalService {

    @Autowired
    BaseService baseService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    //根据前端传出来的信息创建新的责任人
    public Boolean createPrincipal(String name,String mail,String phone){
        String sql="INSERT INTO Principal (name,mail,phone) VALUES(?,?,?)";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql,name,mail,phone);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }

    //根据修改责任人相关信息
    public Boolean updatePrincipalId(Integer principalId,String name,String mail,String phone){
        String sql="UPDATE Principal SET name=?,mail=?,phone=? WHERE id=?";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql,name,mail,phone,principalId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }

    //根据房间ID来删除责任人AdminController
    public Boolean deletePrincipalById(Integer id){
        String sql="DELETE FROM Principal WHERE id=?";
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
