package com.cloud.lz.orderservice;

import com.cloud.lz.orderservice.pojo.Order;
import com.cloud.lz.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {
    @Autowired
    private OrderService orderService;
    @Test
    void contextLoads() {
    }

    @Test
    void testSaveOrder(){
        Order orderById = orderService.getOrderById(1);
        System.out.println(orderById.getMoney());
    }

}
