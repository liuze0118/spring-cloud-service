package com.cloud.lz.advisor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.stereotype.Component;

@Component
public class DistributedLockAdvisor {

    @Around(value = "pointcut()&&@annotation(distributedTransactional)")
    private void doAroundAdvisor(ProceedingJoinPoint joinPoint){
        //获取redis锁
    }

}
