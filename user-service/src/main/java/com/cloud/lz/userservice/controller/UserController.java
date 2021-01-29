package com.cloud.lz.userservice.controller;

import com.cloud.lz.lock.RedisLock;
import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.LogService;
import com.cloud.lz.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
//@RefreshScope
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private RedisLock redisLock;
//    @Value("${dev.ooc:1}")
//    private String oos;
//    @Value("${dev.ood:2}")
//    private String ood;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private LogService logService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/rpc")
    @ResponseBody
    public Map testRpc(){
        String url = "http://order-service/order/get/1";
        Map map = restTemplate.getForObject(url, Map.class);
        System.out.println(map.get("id").toString()+map.get("code")+map.get("money"));
        return map;
    }

    @RequestMapping("/redis/lock/{name}")
    public String testRedisLock(@PathVariable String name) throws InterruptedException {
        if(redisLock.tryLock(name,60)){
//            for (int i = 0; i < 10; i++) {
//                try {
//                    Thread.sleep(i*1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            redisLock.unlock(name);
        }
//        CountDownLatch countDownLatch = new CountDownLatch(10);
//        for (int j = 0; j < 10; j++) {
//            final String str = "redis_lock_" + name +"_"+ j;
//            new Thread(()->{
//                try {
//                    countDownLatch.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if(redisLock.tryLock(str,60*1000)){
//                    for (int i = 0; i < 10; i++) {
//                        try {
//                            Thread.sleep(i*1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    redisLock.unlock(str);
//                }
//            });
//            countDownLatch.countDown();
//        }
        return null;
    }

    @RequestMapping("/redis/{key}")
    public String testRedisCluster(@PathVariable String key){
        Set keys = redisTemplate.opsForCluster().keys(new RedisClusterNode("47.94.237.238", 7001), "*");
        keys.parallelStream().forEach(k->{
            System.out.println("7001---" + k);
        });
        String str = null;
        if(!redisTemplate.opsForValue().setIfAbsent(key, UUID.randomUUID().toString().substring(0,4))){
            Object o=redisTemplate.opsForValue().get(key);
            str = o.toString();
        }else{
            redisTemplate.opsForValue().set(key,UUID.randomUUID().toString().substring(0,4));
            str = redisTemplate.opsForValue().get(key).toString();
        }
        return str;
    }

    @RequestMapping("/sleuth/{str}")
    public String testSleuthLog(@PathVariable("str") String str){
        log.info("sleuth-log---------->" + str);
        return "Sleuth-test";
    }


    @RequestMapping("get/{id}")
    @ResponseBody
    public UserPojo getById(@PathVariable("id") int id){
        UserPojo userById = userService.getUserById(id);
//        userById.setName(oos);
//        userById.setPassword(ood);
        return userById;
    }

    @PostMapping("/getList")
    @ResponseBody
    public List<UserPojo> getUserList(@RequestBody UserPojo userPojo){
        return userService.getListUser(userPojo);
    }

    @RequestMapping("/add")
    public void addUser(@RequestBody UserPojo userPojo){
        userService.addUser(userPojo);
    }

    @RequestMapping("/addTest")
    public void addUserTest(@RequestBody UserPojo userPojo){
        userService.addDistributedUser(null,userPojo);
    }

}
