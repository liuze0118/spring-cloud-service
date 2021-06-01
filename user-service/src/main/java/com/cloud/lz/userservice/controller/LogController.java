package com.cloud.lz.userservice.controller;

import com.cloud.lz.userservice.service.LogService;
import org.apache.tomcat.util.security.MD5Encoder;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.hibernate.loader.plan.spi.QuerySpaceUidNotRegisteredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.security.rsa.RSASignature;

import java.util.UUID;

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

    public static void main(String[] args) {
        String s = UUID.nameUUIDFromBytes("liuze0118".getBytes()).toString();
        MD5Digest md5Digest = new MD5Digest();
        System.out.println("s="+s);
        System.out.println("encode="+1);
    }
}
