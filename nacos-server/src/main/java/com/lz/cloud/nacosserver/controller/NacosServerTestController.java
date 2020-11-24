package com.lz.cloud.nacosserver.controller;

import com.lz.cloud.nacosserver.pojo.UserPojo;
import com.lz.cloud.nacosserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
public class NacosServerTestController {
    @Autowired
    private UserService userService;

    @RequestMapping("/test")
    public String test(){
        return "Nacos-Server";
    }
    @RequestMapping("/seata")
    public String saveUser(String name,int id){
        UserPojo userPojo = new UserPojo();
        userPojo.setId(id);
        userPojo.setName(name);
        userService.addUser(userPojo);
        System.out.println("insert user -----> 事务已经提交");
        return "OK";
    }

}
