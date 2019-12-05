package com.citic.conference.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminController {

    @PostMapping("/adminLogin")
    public Map adminLogin(String adminJson){
//        return loginService.loginAuth(adminJson);
        JSONObject jsonObject = JSON.parseObject(adminJson);
        HashMap map = new HashMap();
        map.put("status","0");
        map.put("name",jsonObject.getString("admin_name"));
        map.put("msg","登录成功");
        return map;
    }

}
