package com.lz.cloud.spiserver;

import com.lz.cloud.spiserver.service.AuthDataSyncService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.ServiceLoader;

@SpringBootTest
class SpiServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void spiTest(){
        ServiceLoader<AuthDataSyncService> loader = ServiceLoader.load(AuthDataSyncService.class);
        Iterator<AuthDataSyncService> iterator = loader.iterator();
        if(iterator.hasNext()) {
            AuthDataSyncService single  = iterator.next();
            single.synchronizationUserData();
            //configurableListableBeanFactory.registerSingleton(single.getClass().getSimpleName(),single);
        }
    }

}
