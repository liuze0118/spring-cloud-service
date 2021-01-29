package com.cloud.lz.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@EnableAsync
@Configuration
public class ScheduledConfig {

    @Bean("taskScheduler")
    public TaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.initialize();
        return taskScheduler;
    }
    @Bean("async")
    public Executor executorService(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 10, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>(10));
        return threadPoolExecutor;
        //return Executors.newFixedThreadPool(10);
    }

}
