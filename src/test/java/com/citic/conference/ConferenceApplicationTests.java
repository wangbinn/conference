package com.citic.conference;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ConferenceApplicationTests {

    @Autowired
    BaseService baseService;
//    @Autowired
//    ProcedureParam procedureParam;

    @Test
    void contextLoads() {

        judgeMeetingRoomTime("2019-12-02 08:00","2019-12-02 12:00");
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
        ProcedureContext bookTimeSelect =
                baseService.callProcedureWithOutParams("Book_Time_Select");
        JSONArray datas = bookTimeSelect.getDatas();
        for (int i=0;i<datas.size();i++){
            Map map=(Map)datas.get(i);
        }
        System.out.println(datas);
    }
}
