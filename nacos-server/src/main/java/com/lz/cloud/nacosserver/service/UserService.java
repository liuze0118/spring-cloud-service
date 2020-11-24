package com.lz.cloud.nacosserver.service;


import com.lz.cloud.nacosserver.pojo.UserPojo;

import java.util.List;

public interface UserService {

    UserPojo getUserById(int id);

    void addUser(UserPojo userPojo);

    void addDistributedUser(String signNatureId,UserPojo userPojo);

    List<UserPojo> getListUser(UserPojo userPojo);

}
