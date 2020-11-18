package com.lz.cloud.spiserver.controller;

import com.lz.cloud.spiserver.lock.RedisLock;
import com.lz.cloud.spiserver.service.AuthDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthDataSyncController {

    @Value("${spring.skip-scan}")
    private String value;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AuthDataSyncService authDataSyncService;

    @Resource
    private RedisLock redisLock;

    @RequestMapping("/user")
    public boolean syncUserData(){
        if(redisLock.lock("redisLock",5,10*60*1000)){
            return authDataSyncService.synchronizationUserData();
        }else {
            return true;
        }

    }

}
