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

    @RequestMapping("/listByDate")
    @ResponseBody
    public Map listByDate(String startDate,String endDate){
        JSONArray datas = bookingService.conditionBookInfo(startDate,endDate);
        System.out.println("startDate:"+startDate+",endDate:"+endDate);
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count",datas.size());
        return map;
    }

}
