package com.citic.conference.service;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingRecordService {

    @Autowired
    private BaseService baseService;

    public JSONArray record(String name) {
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,name, Types.VARCHAR, "IN");
        System.out.println("name"+name);
        pm.add(pp1);
        ProcedureContext scheduledRecord =
                baseService.callProcedure("Select_ScheduledRecord", pm);
        JSONArray datas = scheduledRecord.getDatas();
        System.out.println(datas);
        return datas;
    }

}
