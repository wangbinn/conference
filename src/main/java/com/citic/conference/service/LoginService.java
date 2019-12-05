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

/**登录/注册服务：
 * 1、检验用户的登录
 * 2、将新用户注册信息存储到用户表
 *
 */
@Service
public class LoginService {

    @Autowired
    private BaseService baseService;

    public JSONArray loginAuth(String name,String password){
        List<ProcedureParam> pm = new ArrayList<ProcedureParam>();
        ProcedureParam pp1 = new ProcedureParam(1,name, Types.VARCHAR, "IN");
        ProcedureParam pp2 = new ProcedureParam(2,password,Types.VARCHAR, "IN");
        pm.add(pp1);
        pm.add(pp2);
        ProcedureContext user_login = baseService.callProcedure("User_Login", pm);
        JSONArray datas = user_login.getDatas();
        return datas;
    }

}
