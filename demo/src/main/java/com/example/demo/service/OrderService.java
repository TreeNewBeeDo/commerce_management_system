package com.example.demo.service;

import com.example.demo.model.Order;

import java.util.List;

/**
 * @Auther 石诗佳
 * @Date 2019/1/3 16 57
 * @Desription
 */
public interface OrderService {
    /**
     * 插入订单
     * @param order
     * @return
     */
    int  insertOrder(Order order);

    /**
     * 更新订单状态为确认收货
     * @param order
     * @return
     */
    int confirmOrder(Order order);

    /**
     * 根据userId 和 status查询订单
     * @param status
     * @param userId
     * @return
     */
    List<Order> findOrders(int status, String userId);

    /**
     * 根据orderId查找订单
     * @param orderId
     * @return
     */
    Order findOrder(String orderId);

}
