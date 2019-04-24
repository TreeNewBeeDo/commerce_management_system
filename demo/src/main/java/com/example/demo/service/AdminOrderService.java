package com.example.demo.service;

import com.example.demo.model.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DJS
 * 2018/12/31 12：00
 * 管理员订单管理接口
 */
@Component

public interface AdminOrderService {
    /**
     * 获得订单列表
     * @param status
     * @return
     */
    List<Order> getOrderAndOrderItem(Integer status);

    /**
     * 查找订单
     * @param seekContent
     * @param seekType
     * @return
     */
    List<Order> seekOrder(String seekContent, Integer seekType);

    /**
     * 更改订单status
     * @param order 订单信息   只包含orderId和status
     */
    void orderShip(Order order);

    /**
     * 根据productId获得不同状态订单下产品的数量
     * @param status    订单状态 1下单（未发货）， 2发了货的 ，3完成的 ， 4请求退货的 ， 5退货完成的
     * @param productId
     * @return
     */
    Integer countsOrderProductByStatus(Integer status,Integer productId);

    /**
     * 获得订单详细信息
     */
    Order getOrderInfo(String orderId);

    /**
     * 获得订单对应状态的数量
     * @param status 订单状态 1下单（未发货）， 2发了货的 ，3完成的 ， 4请求退货的 ， 5退货完成的
     * @return
     */
    Integer countOfOrders(Integer status);

    Integer countsAskReturnOrders();
}
