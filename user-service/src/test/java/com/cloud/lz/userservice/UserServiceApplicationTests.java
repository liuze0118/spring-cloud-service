package com.cloud.lz.userservice;

import com.cloud.lz.config.Minio;
import com.cloud.lz.config.StorageConfig;
import com.cloud.lz.lock.RedisLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserServiceApplicationTests {
    @Resource
    private RedisLock redisLock;

    @Resource
    private StorageConfig.StorageProperties storageProperties;
    @Resource
    private Minio minio;

    @Test
    void contextLoads() {
    }

    @Test
     void testProperties(){
        System.out.println(minio.getBucket());
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
