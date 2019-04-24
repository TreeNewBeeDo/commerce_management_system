package com.example.demo.service.impl;

import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderExample;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther 石诗佳
 * @Date 2019/1/3 16 58
 * @Desription
 */
@Service
public class OrderServiceImpl implements OrderService {


    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    @Override
    public int insertOrder(Order order) {
        //OrderExample orderExample=new OrderExample();
        return  orderMapper.insertSelective(order);

    }

    /**
     * 石诗佳
     *  修改状态为确认收货
     * @param order
     * @return
     */
    @Override
    public int confirmOrder(Order order){
        return  orderMapper.updateByPrimaryKeySelective(order);
    }


    @Override
    public List<Order> findOrders(int status, String userId){



        OrderExample example=new OrderExample();
        OrderExample.Criteria criteria=example.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andStatusEqualTo(status);
        List<Order> orders =orderMapper.selectByExample(example);
        return  orders;

    }
    @Override
    public Order findOrder(String orderId){
        return orderMapper.selectByPrimaryKey(orderId);
    }



}
