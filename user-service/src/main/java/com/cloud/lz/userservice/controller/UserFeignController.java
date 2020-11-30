package com.cloud.lz.userservice.controller;

import com.cloud.lz.dto.UserDto;
import com.cloud.lz.feign.service.UserServiceFeign;
import com.cloud.lz.userservice.pojo.UserPojo;
import com.cloud.lz.userservice.service.UserService;
import com.cloud.lz.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@Slf4j
public class UserFeignController implements UserServiceFeign {
    @Autowired
    private UserService userService;
    @Override
    public UserVo getById(int id) {
        log.info("user-service 根据id查询用户--------");
        UserPojo userPojo = userService.getUserById(id);
        UserVo userVo = new UserVo();
        userVo.setId(userPojo.getId());
        userVo.setName(userPojo.getName());
        userVo.setCode(userPojo.getPhoneNumber());
        userVo.setNo(userPojo.getNickName());
        return userVo;
    }

    @Override
    public List<UserVo> getByCondition(UserDto dto) {
        return null;
    }
}
