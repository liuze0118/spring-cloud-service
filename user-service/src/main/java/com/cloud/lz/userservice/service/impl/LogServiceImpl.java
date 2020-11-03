package com.cloud.lz.userservice.service.impl;

import com.cloud.lz.userservice.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogServiceImpl implements LogService {

    @Scheduled(fixedRateString = "5000",initialDelay = 1000)
    public void logstashTask(){
        System.out.println("测试logstash=====");
        log.info("测试logstash" + System.currentTimeMillis());
    }

}
