package com.lz.cloud.nacosserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.lz.cloud.nacosserver.dao.UserDao;
import com.lz.cloud.nacosserver.pojo.UserPojo;
import com.lz.cloud.nacosserver.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;

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


    @Override
    public UserPojo getUserById(int id) {
        System.out.println("ooc =" + ooc);
        System.out.println("ood =" + ood);
        UserPojo one = userDao.getOne(id);
        one.setName(ooc);
        one.setPassword(ood);
        return one;
    }

    @Override
    @GlobalTransactional
    public void addUser(UserPojo userPojo) {
        userDao.save(userPojo);
        if(userPojo.getId() == 5)
            throw new RuntimeException("测试回滚");
    }
    @Override
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

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        UserPojo userPojo = new UserPojo();
        userPojo.setId(1);
        Class<?> clazz  = userPojo.getClass();
        Field id = clazz.getDeclaredField("id");
        id.setAccessible(true);
        String  o = String.valueOf(id.get(userPojo));
        System.out.println(o);
    }

}
