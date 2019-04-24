package com.example.demo.mapper;

import com.example.demo.model.OrderItem;
import com.example.demo.model.OrderItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int countByExample(OrderItemExample example);

    int deleteByExample(OrderItemExample example);

    int deleteByPrimaryKey(String itemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByExample(OrderItemExample example);

    /**
     * 此方法用于改 OrderMappper.xml文件   关联映射查订单项目用
     * @param orderId
     * @return
     */
    List<OrderItem> selectByOrderId(String orderId);

    OrderItem selectByPrimaryKey(String itemId);

    int updateByExampleSelective(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    int updateByExample(@Param("record") OrderItem record, @Param("example") OrderItemExample example);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}