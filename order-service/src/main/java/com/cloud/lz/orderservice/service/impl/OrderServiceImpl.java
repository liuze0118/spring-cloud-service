package com.cloud.lz.orderservice.service.impl;

import com.cloud.lz.annotation.DistributedTransactional;
import com.cloud.lz.orderservice.dao.OrderDao;
import com.cloud.lz.orderservice.pojo.Order;
import com.cloud.lz.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public Order getOrderById(int id) {
        return orderDao.getOne(id);
    }

    @Override
    public List<Order> getOrderList(Order order) {
        return orderDao.findAll();
    }

    @Override
    @DistributedTransactional(type = "provider")
    public Order saveOrder(String signNatureId,Order order) {
        Order save = orderDao.save(order);
        if(save.getId() == 5)
            throw new RuntimeException("测试回滚");
        return save;
    }
}
