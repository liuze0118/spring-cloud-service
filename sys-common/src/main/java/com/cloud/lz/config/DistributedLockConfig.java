package com.cloud.lz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class DistributedLockConfig {
    @Autowired
    private RedisTemplate redisTemplate;






}
