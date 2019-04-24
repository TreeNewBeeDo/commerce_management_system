package com.example.demo.service.impl;

import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.model.OrderItem;
import com.example.demo.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther asus
 * @Date 2019/1/3 16 53
 * @Desription
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMapper orderItemMapper;

    @Autowired
    public  OrderItemServiceImpl(OrderItemMapper orderItemMapper){
        this.orderItemMapper=orderItemMapper;
    }

    /**
     * 插入订单明细表
     * @param record
     * @return
     */
    @Override
    public int insertOrderItem(OrderItem record){

        return orderItemMapper.insert(record);

    }


}
