package com.cloud.lz.userservice.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONStreamAware;
import com.cloud.lz.annotation.DistributedTransactional;
import com.cloud.lz.userservice.dao.UserDao;
import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.UserService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.Executor;

@RefreshScope
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Value("${dev.ooc:1}")
    private String ooc;
    @Value("${dev.ood:2}")
    private String ood;

    @Autowired
    private UserService userService;

    @Override
    @SentinelResource(value = "getUser",blockHandler = "getUserBlock", fallback = "getUserFallBack")
    public UserPojo getUserById(int id) {
//        System.out.println("ooc =" + ooc);
//        System.out.println("ood =" + ood);
//        one.setName(ooc);
//        one.setPassword(ood);
        UserPojo one = new UserPojo();
        one.setId(id);
        one.setName("liuze"+id);
        return one;
    }

    public UserPojo getUserFallBack(int id){
        System.out.println("限流成功,服务降级--------");
        return null;
    }


    public UserPojo getUserBlock(int id){
        System.out.println("接口被限流------------");
        return null;
    }


    @Override
    @DistributedTransactional
    public void addUser(UserPojo userPojo) {
        userDao.save(userPojo);
        if(userPojo.getId() == 5)
            throw new RuntimeException("测试回滚");
    }
    @Override
    @DistributedTransactional
    public void addDistributedUser(String signNatureId,UserPojo userPojo) {
        RestTemplate restTemplate = new RestTemplate();
        userDao.save(userPojo);
        String url = "http://localhost:8082/order/save/"+signNatureId;
        String order = "{\n" +
                "    \"id\": "+(userPojo.getId()+1)+",\n" +
                "    \"code\": \"test_tx\",\n" +
                "    \"money\": \"23.01\"\n" +
                "}";
        ResponseEntity<Object> entity = restTemplate.postForEntity(url, JSON.parseObject(order), null);
        if(entity.getStatusCode().value() != 200)
            throw new RuntimeException("do rest failed");
        if(userPojo.getId() == 5)
            throw new RuntimeException("测试回滚");
    }

    @Override
    public List<UserPojo> getListUser(UserPojo userPojo) {
        return userDao.queryAllByIdIsOrNameLike(userPojo.getId(),userPojo.getName());
    }

//    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
//        UserPojo userPojo = new UserPojo();
//        userPojo.setId(1);
//        Class<?> clazz  = userPojo.getClass();
//        Field id = clazz.getDeclaredField("id");
//        id.setAccessible(true);
//        String  o = String.valueOf(id.get(userPojo));
//        System.out.println(o);
//        String str = "test_11";
//        System.out.println(str.contains("test_"));
//    }

}
