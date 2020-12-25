package com.cloud.lz.userservice;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.cloud.lz.config.StorageConfig;
import com.cloud.lz.lock.RedisLock;
import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class UserServiceApplicationTests {
    @Autowired(required = false)
    private List<RedisLock> redisLocks;

    @Resource
    private StorageConfig.StorageProperties storageProperties;

    @Autowired
    private UserService userService;


    @Test
    void contextLoads() {
    }

    @Test
    void testSentinel(){
        CountDownLatch countDownLatch = new CountDownLatch(10);
        List<FlowRule> rules = new ArrayList<FlowRule>();
        FlowRule rule1 = new FlowRule();
        rule1.setResource("getUser");
        // set limit qps to 20
        rule1.setCount(4);
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setLimitApp("default");
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
        for (int i = 0; i < 11; i++) {
            UserPojo user = userService.getUserById(i);
            System.out.println(user);
//            int j = i;
//            new Thread(()->{
//                try {
//                    countDownLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                UserPojo user1 = userService.getUserById(j);
//                System.out.println(user.toString());
//            }).start();
            countDownLatch.countDown();
        }
    }

    @Test
     void testProperties(){
        System.out.println(storageProperties.getMinioPropertie().getBucket());
    }

    @Test
    void testRedisLock() throws InterruptedException {
//        String type = storageProperties.getType();
//        StorageConfig.StorageMinioProperties minioPropertie = storageProperties.getMinioPropertie();
//        System.out.println();
//        String name = "redis_lock";
//        if(redisLock.tryLock(name,30,true)){
//            for (int i = 0; i < 10; i++) {
//                Thread.sleep(i*2*1000);
//            }
//            redisLock.unlock(name);
//        }
//        while (true){
//
//        }

    }

}
