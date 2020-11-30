package com.cloud.lz.userservice.service.impl;

import com.cloud.lz.userservice.controller.UserController;
import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Autowired
    private UserController userController;

//    @Scheduled(fixedRateString = "5000",initialDelay = 1000)
    public void logstashTask(){
        UserPojo userPojo = userController.getById(1);
        System.out.println(userPojo.getName());
    }

}
