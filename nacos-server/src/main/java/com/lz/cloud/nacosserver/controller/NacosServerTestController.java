package com.lz.cloud.nacosserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nacos")
public class NacosServerTestController {
    @RequestMapping("/test")
    public String test(){
        return "Nacos-Server";
    }

}
