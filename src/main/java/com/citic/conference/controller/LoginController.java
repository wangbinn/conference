package com.citic.conference.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.citic.conference.database.BaseService;
import com.citic.conference.database.ProcedureContext;
import com.citic.conference.database.ProcedureParam;
import com.citic.conference.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/loginAuth")
    public Map loginAuth(String userJson){
        return loginService.loginAuth(userJson);
    }

    @GetMapping("/logout")
    public Map logout(){
        HashMap map = new HashMap();
        map.put("status","0");
        map.put("msg","退出成功");
        return map;
    }

    @PostMapping("/register")
    public Map register(String userJson){
        HashMap map = new HashMap();
        return map;
    }

}
