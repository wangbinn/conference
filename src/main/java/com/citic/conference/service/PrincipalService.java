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

    //查询所有责任人相关信息
    public JSONArray allBookInfo(){
        ProcedureContext initialization = baseService.callProcedureWithOutParams("Book_Initialization");
        JSONArray datas = initialization.getDatas();
        System.out.println(datas);
        return datas;
    }

    //根据责任人Id来查其相关信息
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

    //根据前端传出来的信息创建新的责任人
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

    //根据修改责任人相关信息
    public Boolean updateRoomId(Integer roomId,Integer principalId,
                                String floor,String roomNumber,Integer seatNumber, Integer multimedia){
        String sql="UPDATE MeetingRoom SET floor=?,roomNumber=?,seatNumber=?,principalId=?,multimedia=? WHERE id=?";
        int register=0 ;
        try {
            register=jdbcTemplate.update(sql,floor,roomNumber,seatNumber,principalId,multimedia,roomId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (register==1){
            return true;
        }else
            return false;
    }

    //根据房间ID来删除责任人
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
