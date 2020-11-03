package com.cloud.lz.orderservice.service;

import com.cloud.lz.orderservice.pojo.Order;

import java.util.List;

public interface OrderService {
    public Order getOrderById(int id);

    public List<Order> getOrderList(Order order);

    public Order saveOrder(String signNatureId,Order order);
}
