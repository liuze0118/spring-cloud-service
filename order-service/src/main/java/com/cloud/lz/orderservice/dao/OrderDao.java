package com.cloud.lz.orderservice.dao;

import com.cloud.lz.orderservice.pojo.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
}
