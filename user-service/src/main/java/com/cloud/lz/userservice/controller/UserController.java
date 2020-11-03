package com.cloud.lz.userservice.controller;

import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.LogService;
import com.cloud.lz.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
//@RefreshScope
public class UserController {

    @Autowired
    private UserService userService;
//    @Value("${dev.ooc:1}")
//    private String oos;
//    @Value("${dev.ood:2}")
//    private String ood;

    @Autowired
    private Environment environment;

    @Autowired
    private LogService logService;

    @RequestMapping("get/{id}")
    @ResponseBody
    public UserPojo getById(@PathVariable("id") int id){
        UserPojo userById = userService.getUserById(id);
//        userById.setName(oos);
//        userById.setPassword(ood);
        logService.logstashTask();
        return userById;
    }

    @PostMapping("/getList")
    @ResponseBody
    public List<UserPojo> getUserList(@RequestBody UserPojo userPojo){
        return userService.getListUser(userPojo);
    }

    @RequestMapping("/add")
    public void addUser(@RequestBody UserPojo userPojo){
        userService.addUser(userPojo);
    }

    @RequestMapping("/addTest")
    public void addUserTest(@RequestBody UserPojo userPojo){
        userService.addDistributedUser(null,userPojo);
    }

}
