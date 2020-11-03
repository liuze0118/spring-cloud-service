package com.cloud.lz.userservice.dao;

import com.cloud.lz.userservice.pojo.UserPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<UserPojo,Integer> {

    List<UserPojo> queryAllByIdIsOrNameLike(int id, String name);

}
