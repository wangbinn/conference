package com.citic.conference.controller;

import com.citic.conference.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @RequestMapping("/test")
    public String sayHello(){
        return "Hello!";
    }

    @RequestMapping("/testJson")
    public Map testJson(){
        User user1 = new User();
        user1.setId(1);
        user1.setName("hhh");
        user1.setPassword("456");
        User user2 = new User();
        user2.setId(2);
        user2.setName("www");
        user2.setPassword("123");
        List list = new ArrayList();
        list.add(user1);
        list.add(user2);
        HashMap map = new HashMap();
        map.put("status","0");
        map.put("data",list);
        return map;
    }

}
