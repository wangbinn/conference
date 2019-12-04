package com.citic.conference.controller;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import com.citic.conference.entity.MeetingRoom;
import com.citic.conference.entity.User;
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

//    @RequestMapping("/list")
//    @ResponseBody
//    public Map conferenceList(Integer page, Integer limit){
//        MeetingRoom meetingRoom1 = new MeetingRoom();
//        meetingRoom1.setId(1);
//        meetingRoom1.setFloor("十楼");
//        meetingRoom1.setRoomNumber("8866");
//        meetingRoom1.setPrincipalId(2);
//        meetingRoom1.setSeatNumber(32);
//        meetingRoom1.setMultimedia(0);
//        MeetingRoom meetingRoom2 = new MeetingRoom();
//        meetingRoom2.setId(2);
//        meetingRoom2.setFloor("七楼");
//        meetingRoom2.setRoomNumber("856");
//        meetingRoom2.setPrincipalId(3);
//        meetingRoom2.setSeatNumber(16);
//        meetingRoom2.setMultimedia(1);
//        MeetingRoom meetingRoom3 = new MeetingRoom();
//        meetingRoom3.setId(3);
//        meetingRoom3.setFloor("十二楼");
//        meetingRoom3.setRoomNumber("1266");
//        meetingRoom3.setPrincipalId(1);
//        meetingRoom3.setSeatNumber(26);
//        meetingRoom3.setMultimedia(0);
//        ArrayList list = new ArrayList();
//        list.add(meetingRoom1);
//        list.add(meetingRoom2);
//        list.add(meetingRoom3);
//        HashMap map = new HashMap();
//        map.put("code","0");
//        map.put("msg","this is msg");
//        map.put("data",list);
//        map.put("count","3");
//        return map;
//    }

    @Autowired
    BaseService baseService;

    @RequestMapping("/list")
    @ResponseBody
    public Map conferenceList(Integer page, Integer limit){
        ProcedureContext initialization = baseService.callProcedureWithOutParams("Book_Initialization");
        JSONArray datas = initialization.getDatas();
        System.out.println(datas);
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count","3");
        return map;
    }

    @RequestMapping("/history")
    @ResponseBody
    public Map historyList(String name){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,"小明", Types.VARCHAR, "IN");
        pm.add(pp1);
        ProcedureContext scheduledRecord =
                baseService.callProcedure("Select_ScheduledRecord", pm);
        JSONArray datas = scheduledRecord.getDatas();
        System.out.println(datas);
        HashMap map = new HashMap();
        map.put("code","0");
        map.put("msg","this is msg");
        map.put("data",datas);
        map.put("count","2");
        return map;
    }

}
