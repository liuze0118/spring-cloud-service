package com.cloud.lz.orderservice.controller;

import com.cloud.lz.orderservice.pojo.Order;
import com.cloud.lz.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get/{id}")
    @ResponseBody
    public Order getOrder(@PathVariable int id){
        Order orderById = orderService.getOrderById(id);
        return orderById;
    }

    @RequestMapping("/getList")
    @ResponseBody
    public List<Order> getOrderList(@RequestBody Order order){
        List<Order> orderList = orderService.getOrderList(order);
        return orderList;
    }

    @PostMapping("/save/{signNatureId}")
    public void saveOrder(@PathVariable String signNatureId,@RequestBody Order order){
        orderService.saveOrder(signNatureId,order);
    }




}
