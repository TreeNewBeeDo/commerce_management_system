package com.example.demo.service;

import com.example.demo.model.OrderItem;
import org.springframework.stereotype.Component;

/**
 * @Auther 石诗佳
 * @Date 2019/1/3 16 51
 * @Desription
 */
@Component
public interface OrderItemService {
    /**
     * 插入订单明细表
     * @param record
     * @return
     */
    int insertOrderItem(OrderItem record);
}
