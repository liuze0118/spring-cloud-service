package com.cloud.lz.feign.service;

import com.cloud.lz.dto.UserDto;
import com.cloud.lz.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("user-service")
@RequestMapping("/user/feign")
public interface UserServiceFeign {
    @RequestMapping("/get/{id}")
    @ResponseBody
    public UserVo getById(@PathVariable int id);

    @RequestMapping("/getCondition")
    @ResponseBody
    public List<UserVo> getByCondition(@RequestBody UserDto dto);

}
