package com.cloud.lz.userservice;

import com.cloud.lz.lock.RedisLock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceApplicationTests {
    @Resource
    private RedisLock redisLock;

    @Test
    void contextLoads() {
    }

    @Test
    void testRedisLock() throws InterruptedException {

        String name = "redis_lock";
        if(redisLock.tryLock(name,30,true)){
            for (int i = 0; i < 10; i++) {
                Thread.sleep(i*2*1000);
            }
            redisLock.unlock(name);
        }
        while (true){

        }

    }

}
