package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;

@RestController
public class MainApi {
    private Map<String,Object> params = new HashMap<>();

    //无参方式
    @GetMapping(value = "/getuser")
    public Object getuser(@RequestParam(required = true, value = "userID", defaultValue = "") String userID) {
        params.clear();
        params.put("id", "room");
        System.out.println("用户登录");
        return "1";
    }

    @GetMapping(value = "/getpackager")
    public Object getpackager(@RequestParam(required = true, value = "userID", defaultValue = "") String userID) {
        params.clear();
        params.put("id", "page");
        System.out.println("快递员登录");
        return "1";
    }

    @GetMapping(value = "/getmanager")
    public Object getmanager(@RequestParam(required = true, value = "userID", defaultValue = "") String userID) {
        params.clear();
        params.put("id", "page");
        System.out.println("管理员登录");
        return "1";
    }
}