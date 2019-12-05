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
public class AdminService {

    @Autowired
    BaseService baseService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    //管理者登录
    public JSONArray adminLogin(String name, String password){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,name, Types.VARCHAR, "IN");
        ProcedureParam pp2 = new ProcedureParam(2,password,Types.VARCHAR, "IN");
        pm.add(pp1);
        pm.add(pp2);
        ProcedureContext user_login = baseService.callProcedure("AdminUser_Login", pm);
        JSONArray datas = user_login.getDatas();
        return datas;
    }

    //查询所有会议室相关信息
    public JSONArray allBookInfo(){
        ProcedureContext initialization = baseService.callProcedureWithOutParams("Book_Initialization");
        JSONArray datas = initialization.getDatas();
        System.out.println(datas);
        return datas;
    }

    //根据查询的房间号来查会议室相关信息
    public JSONArray singleBookInfo(String roomNo){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,roomNo, Types.VARCHAR, "IN");
        //System.out.println("name"+name);
        pm.add(pp1);
        ProcedureContext scheduledRecord =
                baseService.callProcedure("Select_MeetRoomInfo", pm);
        JSONArray datas = scheduledRecord.getDatas();
        System.out.println(datas);
        return datas;
    }

    //根据前端传出来的信息创建新的会议室
    public Boolean createRoom(Integer principalId,String floor,String roomNumber,
                              Integer seatNumber,Integer multimedia){
        //将新用户信息注册到用户表
        String sql="INSERT INTO MeetingRoom (floor,roomNumber,seatNumber,principalId,multimedia) VALUES(?,?,?,?,?)";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql,floor,roomNumber,seatNumber,principalId,multimedia);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }

    //根据房间ID来删除议室相关信息
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
