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

import javax.servlet.http.HttpSession;
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
        HashMap map = new HashMap();
        JSONObject jsonObject = JSON.parseObject(userJson);
        String name=jsonObject.getString("user_name");
        String password=jsonObject.getString("user_password");
        JSONArray datas=loginService.loginAuth(name,password);
        for (int i=0;i<datas.size();i++){
            Map o = (Map)datas.get(i);
            //System.out.println(o.get(""));
            if (o.get("").equals("1")){
               // System.out.println("用户存在");
                map.put("status","0");
                break;
            }
            if ((!o.get("").equals("-1"))&(!o.get("").equals("1"))){
               // System.out.println("用户密码错误，密码提示为："+o.get(""));
                map.put("status","1");
                map.put("msg","用户密码错误，密码提示为："+o.get(""));
                break;
            }
            if(i==datas.size()-1){
                //System.out.println("用户未注册");
                map.put("status","2");
                map.put("msg","用户未注册");
            }
        }
        return map;
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
        JSONObject jsonObject = JSON.parseObject(userJson);
        String name=jsonObject.getString("user_name");
        String password=jsonObject.getString("user_password");
        String passwordhimt=jsonObject.getString("password_hint");
        map.put("status","0");
        map.put("msg","注册未成功");
        return map;
    }

}
