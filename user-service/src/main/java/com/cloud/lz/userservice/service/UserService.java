package com.cloud.lz.userservice.service;

import com.cloud.lz.userservice.pojo.UserPojo;

import java.util.List;

public interface UserService {

    UserPojo getUserById(int id);

    void addUser(UserPojo userPojo);

    void addDistributedUser(String signNatureId,UserPojo userPojo);

    List<UserPojo> getListUser(UserPojo userPojo);

}
