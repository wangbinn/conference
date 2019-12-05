package com.citic.conference.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import com.citic.conference.entity.MeetingRoom;
import com.citic.conference.entity.User;
import com.citic.conference.service.BookingRecordService;
import com.citic.conference.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/conference")
public class ConferenceController {

    @Autowired
    private BookingRecordService bookingRecordService;

    @Autowired
    private BookingService bookingService;

    //用户预订会议室
    @RequestMapping("/list")
    @ResponseBody
    public Map conferenceList(String startDate,String endDate){
        JSONArray datas = new JSONArray();
        if (startDate == null || endDate == null){
            datas = bookingService.bookInit();
        }else {
            datas = bookingService.conditionBookInfo(startDate,endDate);
        }
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count",datas.size());
        return map;
    }

    //用户查看已预订的记录
    @RequestMapping("/history")
    @ResponseBody
    public Map historyList(String name){
        JSONArray datas = bookingRecordService.record(name);
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count",datas.size());
        return map;
    }

    //用户取消未过期的预订
    @RequestMapping("/deleteById")
    @ResponseBody
    public Map conferenceList(Integer id){
        HashMap map = new HashMap();
        Boolean idBoolean = bookingRecordService.deleteRecordById(id);
        if (idBoolean==true){
            map.put("status","0");
            map.put("msg","删除成功");
        }else{
            map.put("status","1");
            map.put("msg","删除失败");
        }
        return map;
    }

    //将用户预定信息保存到预定表中
    @RequestMapping("/booking")
    @ResponseBody
    public Map booking(String bookingInfo){
        HashMap map = new HashMap();
        JSONObject jsonObject = JSON.parseObject(bookingInfo);
        Integer id = jsonObject.getInteger("id");
        String userName = jsonObject.getString("userName");
        String startDate = jsonObject.getString("bookingWithStartDate");
        String endDate = jsonObject.getString("bookingWithEndDate");
        String apply=jsonObject.getString("apply");
        Boolean roomBoolean = bookingService.bookingRoom(id, userName, startDate, endDate, apply);
       if (roomBoolean==true){
           map.put("status","0");
           map.put("msg","预订成功");
       }else {
           map.put("status","1");
           map.put("msg","预订失败");
       }
        return map;
    }

}
