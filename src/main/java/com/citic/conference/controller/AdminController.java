package com.citic.conference.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citic.conference.database.BaseService;
import com.citic.conference.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**管理者页面访问处理：
 * 1、管理者登录
 * 2、管理者查询所有会议室信息，查询指定会议室id
 * 3、管理者更新会议室信息，增加会议室
 * 4、管理者删除会议室信息
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    BaseService baseService;
    @Autowired
    AdminService adminService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/adminLogin")
    public Map adminLogin(String adminJson){
//        return loginService.loginAuth(adminJson);
        HashMap map = new HashMap();
        JSONObject jsonObject = JSON.parseObject(adminJson);
        String name=jsonObject.getString("admin_name");
        String password=jsonObject.getString("admin_password");
        JSONArray datas=adminService.adminLogin(name,password);
        for (int i=0;i<datas.size();i++){
            Map o = (Map)datas.get(i);
            //System.out.println(o.get(""));
            if (o.get("").equals("1")){
                // System.out.println("用户存在");
                map.put("status","0");
                map.put("name",name);
                break;
            }
            if ((!o.get("").equals("-1"))&(!o.get("").equals("1"))){
                // System.out.println("用户密码错误，密码提示为："+o.get(""));
                map.put("status","1");
                map.put("msg","用户密码错误，密码提示为："+o.get(""));
                break;
            }
            if(i==datas.size()-1){
                //System.out.println("用户未注册");
                map.put("status","2");
                map.put("msg","您不是管理者，非法操作！");
            }
        }
        return map;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Map conferenceList(String roomNumber){
        JSONArray datas = new JSONArray();
        if (roomNumber == null){
            datas = adminService.allBookInfo();
        }else {
            datas = adminService.singleBookInfo(roomNumber);
        }
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count",datas.size());
        return map;
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public Map conferenceList(Integer id){
        HashMap map = new HashMap();
        Boolean idBoolean = adminService.deleteRoomById(id);
        if (idBoolean==true){
            map.put("status","0");
            map.put("msg","删除成功");
        }else{
            map.put("status","1");
            map.put("msg","删除失败");
        }
        return map;
    }

    @RequestMapping("/meetingRoomCreate")
    @ResponseBody
    public Map meetingRoomCreate(String meetingRoomInfo){
        JSONObject jsonObject = JSON.parseObject(meetingRoomInfo);
        Integer principalId = jsonObject.getInteger("principal");
        String floor = jsonObject.getString("floor");
        String roomNumber = jsonObject.getString("roomNumber");
        Integer seatNumber = jsonObject.getInteger("seatNumber");
        Integer multimedia = jsonObject.getInteger("multimedia");
        //System.out.println(principalId + floor + roomNumber + seatNumber + multimedia);
        Boolean rommBoolean = adminService.createRoom(principalId,
                floor, roomNumber, seatNumber, multimedia);
        HashMap map = new HashMap();
        Boolean idBoolean = true;
        if (rommBoolean==true){
            map.put("status","0");
            map.put("msg","添加成功");
        }else{
            map.put("status","1");
            map.put("msg","房间号冲突，添加失败！");
        }
        return map;
    }

    @RequestMapping("/getPrincipal")
    @ResponseBody
    public Map getPrincipal(){
        HashMap map = new HashMap();
        List list = jdbcTemplate.queryForList("select id,name from Principal");
       // System.out.println("Principal:"+list);
        map.put("status","0");
        map.put("data",list);
        return map;
    }

}
