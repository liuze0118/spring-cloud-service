package com.cloud.lz.orderservice.service.impl;

import com.cloud.lz.annotation.DistributedTransactional;
import com.cloud.lz.feign.service.UserServiceFeign;
import com.cloud.lz.orderservice.dao.OrderDao;
import com.cloud.lz.orderservice.pojo.Order;
import com.cloud.lz.orderservice.service.OrderService;
import com.cloud.lz.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Resource
    private UserServiceFeign userServiceFeign;

    @Override
    public Order getOrderById(int id) {
        log.info("order-service feign调用 user-service 服务--------");
        UserVo userVo = userServiceFeign.getById(1);
        log.info("user-service 服务调用成功--------");
        System.out.println(userVo.getName());
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
