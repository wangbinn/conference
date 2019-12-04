package com.citic.conference.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    private BaseService baseService;

    public Map loginAuth(String userJson){
        HashMap map = new HashMap();
        JSONObject jsonObject = JSON.parseObject(userJson);
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,jsonObject.getString("user_name"), Types.VARCHAR, "IN");
        ProcedureParam pp2 = new ProcedureParam(2,jsonObject.getString("user_password"),Types.VARCHAR, "IN");
        pm.add(pp1);
        pm.add(pp2);
        ProcedureContext user_login = baseService.callProcedure("User_Login", pm);
        JSONArray datas = user_login.getDatas();
        for (int i=0;i<datas.size();i++){
            Map o = (Map)datas.get(i);
            //System.out.println(o.get(""));
            if (o.get("").equals("1")){
                System.out.println("用户存在");
                map.put("status","0");
                break;
            }
            if ((!o.get("").equals("-1"))&(!o.get("").equals("1"))){
                System.out.println("用户密码错误，密码提示为："+o.get(""));
                map.put("status","1");
                map.put("msg","用户密码错误，密码提示为："+o.get(""));
                break;
            }
            if(i==datas.size()-1){
                System.out.println("用户未注册");
                map.put("status","2");
                map.put("msg","用户未注册");
            }
        }
        return map;
    }

}
