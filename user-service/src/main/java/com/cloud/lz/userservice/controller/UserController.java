package com.cloud.lz.userservice.controller;

import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.LogService;
import com.cloud.lz.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@Slf4j
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

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/redis/{key}")
    public String testRedisCluster(@PathVariable String key){
        Set keys = redisTemplate.opsForCluster().keys(new RedisClusterNode("47.94.237.238", 7001), "*");
        keys.parallelStream().forEach(k->{
            System.out.println("7001---" + k);
        });
        String str = null;
        if(!redisTemplate.opsForValue().setIfAbsent(key,(Math.random()*100)+1)){
            Object o=redisTemplate.opsForValue().get(key);
            str = o.toString();
        }else{
            redisTemplate.opsForValue().set(key,(Math.random()*100)+1);
            str = redisTemplate.opsForValue().get(key).toString();
        }
        return str;
    }

    @RequestMapping("/sleuth/{str}")
    public String testSleuthLog(@PathVariable("str") String str){
        log.info("sleuth-log---------->" + str);
        return "Sleuth-test";
    }


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
