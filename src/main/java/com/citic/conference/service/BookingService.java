package com.citic.conference.service;

import com.alibaba.fastjson.JSONArray;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BaseService baseService;

    public JSONArray bookInit(){
        ProcedureContext initialization = baseService.callProcedureWithOutParams("Book_Initialization");
        JSONArray datas = initialization.getDatas();
        System.out.println(datas);
        return datas;
    }

}
