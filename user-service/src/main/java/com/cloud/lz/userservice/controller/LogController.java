package com.cloud.lz.userservice.controller;

import com.cloud.lz.userservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;


    @RequestMapping("/test/{id}")
    public String testIOC(@PathVariable Integer id){
        //UserVo userVo = userController.getById(id);
        logService.logstashTask();
        return null;
    }

}
