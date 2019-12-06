package com.citic.conference.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citic.conference.database.BaseService;
import com.citic.conference.service.AdminService;
import com.citic.conference.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**责任人页面访问处理：
 * 1、管理者登录
 * 2、管理者查询所有会议室信息，查询指定会议室id
 * 3、管理者更新会议室信息，增加会议室
 * 4、管理者删除会议室信息
 */
@RestController
@RequestMapping("/prin")
public class PrincipalController {

    @Autowired
    BaseService baseService;
    @Autowired
    PrincipalService principalService;
    @Autowired
    JdbcTemplate jdbcTemplate;

//    @RequestMapping("/list")
//    @ResponseBody
//    public Map conferenceList(String roomNumber){
//        JSONArray datas = new JSONArray();
//        if ((roomNumber == null)||(roomNumber.equals(""))){
//            datas = adminService.allBookInfo();
//        }else {
//            datas = adminService.singleBookInfo(roomNumber);
//        }
//        HashMap map = new HashMap();
//        map.put("code","0");
//        map.put("msg","this is msg");
//        map.put("data",datas);
//        map.put("count",datas.size());
//        return map;
//    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public Map principalDelete(Integer id){
        HashMap map = new HashMap();
        Boolean idBoolean =principalService.deletePrincipalById(id);
        if (idBoolean==true){
            map.put("status","0");
            map.put("msg","删除成功");
        }else{
            map.put("status","1");
            map.put("msg","删除失败");
        }
        return map;
    }

    @RequestMapping("/roomManagerCreate")
    @ResponseBody
    public Map principalCreate(String roomManagerInfo){
        JSONObject jsonObject = JSON.parseObject(roomManagerInfo);
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        String mail = jsonObject.getString("mail");
        //System.out.println(principalId + floor + roomNumber + seatNumber + multimedia);
        Boolean rommBoolean = principalService.createPrincipal(name,mail,phone);
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
        List list = jdbcTemplate.queryForList("select id,name,phone,mail from Principal");
       // System.out.println("Principal:"+list);
        map.put("code","0");
        map.put("data",list);
        map.put("msg","this is msg");
        map.put("count",list.size());
        return map;
    }

    @RequestMapping("/roomManagerUpdate")
    @ResponseBody
    public Map principalUpdate(String roomManagerInfo){
        JSONObject jsonObject = JSON.parseObject(roomManagerInfo);
        Integer id=jsonObject.getInteger("id");
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        String mail = jsonObject.getString("mail");
        System.out.println("id:"+id);
        Boolean updateInfo = principalService.updatePrincipalId(id,name,mail,phone);
        HashMap map = new HashMap();
//        Boolean rommBoolean = adminService.createRoom(principalId,
//                floor, roomNumber, seatNumber, multimedia);

//        Boolean idBoolean = true;
        if (updateInfo==true){
            map.put("status","0");
            map.put("msg","修改成功");
        }else{
            map.put("status","1");
            map.put("msg","修改失败！");
        }
        return map;
    }

}
