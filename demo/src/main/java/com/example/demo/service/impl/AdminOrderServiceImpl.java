package com.example.demo.service.impl;

import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.*;
import com.example.demo.service.AdminOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * DJS
 * 2018/12/31 12:00
 * 管理员订单接口实现类
 */
@Service
public class AdminOrderServiceImpl implements AdminOrderService {


    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdminOrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper
            , ProductMapper productMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.userMapper=userMapper;
    }

    /**
     * 获得不同状态的订单
     * @return
     */
    @Override
    public List<Order> getOrderAndOrderItem(Integer status) {
        List<Order> orderList=new ArrayList<Order>();
        OrderExample orderExample=new OrderExample();
        OrderExample.Criteria criteria=orderExample.createCriteria();
        if(status==3) {
            List<Integer> statusList=new ArrayList<>();
            statusList.add(3);
            statusList.add(6);
            criteria.andStatusIn(statusList);
        }else {
            criteria.andStatusEqualTo(status);
        }
        orderExample.setOrderByClause("order_time desc");
        orderList=orderMapper.selectByExample(orderExample);
//        System.out.println("orderId"+orderList.get(0).getOrderItemList().size());
//        System.out.println("商品价格："+orderList.get(0).getOrderItemList().get(0).getProductOrderItem().getPrice());
        return orderList;
    }


    /**
     * 查找订单
     * @return
     */
    @Override
    public List<Order> seekOrder(String seekContent, Integer seekType) {
        List<Order> orderList=new ArrayList<Order>();
        OrderExample orderExample=new OrderExample();
        orderExample.setOrderByClause("order_time desc");
        OrderExample.Criteria criteria=orderExample.createCriteria();
        if(seekType==1) {
            criteria.andOrderIdEqualTo(seekContent);
            orderList=orderMapper.selectByExample(orderExample);
            return orderList;
        }else if(seekType==2){
            List<String> orderIds=new ArrayList<>();
            List<OrderItem> orderItems=getOrderIdByProductId(seekContent);
            if (orderItems.size()==0){
                return null;
            }
            for (OrderItem orderItem:orderItems) {
                orderIds.add(orderItem.getOrderId());
                System.out.println(orderIds.size());
            }
            criteria.andOrderIdIn(orderIds);
            orderList=orderMapper.selectByExample(orderExample);
            return orderList;
        }else if(seekType==3){
            String userId=getUserIdByUserName(seekContent);
            criteria.andUserIdEqualTo(userId);
            orderList=orderMapper.selectByExample(orderExample);
            return orderList;
        }else return null;
    }

    /**
     * 更改订单status
     * @param order 订单信息   只包含orderId和status
     */
    @Override
    public void orderShip(Order order) {
        System.out.println("订单号："+order.getOrderId()+"状态："+order.getStatus());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 根据productId获得不同状态订单下产品的数量
     * @param status    订单状态 1下单（未发货）， 2发了货的 ，3完成的 ， 4请求退货的 ， 5退货完成的
     * @param productId
     * @return
     */
    @Override
    public Integer countsOrderProductByStatus(Integer status,Integer productId) {
        //获得orders
        Integer counts=0;
        List<Order> orders=new ArrayList<>();
        OrderExample orderExample=new OrderExample();
        OrderExample.Criteria orderExampleCriteria =orderExample.createCriteria();
        if(status==1){
            orderExampleCriteria.andStatusEqualTo(1);
        } else if(status==2){
            orderExampleCriteria.andStatusEqualTo(2);
        } else if(status==3){
            orderExampleCriteria.andStatusEqualTo(3);
        } else if(status==4){
            orderExampleCriteria.andStatusEqualTo(4);
        } else if(status==5){
            orderExampleCriteria.andStatusEqualTo(5);
        } else if(status==6){
            orderExampleCriteria.andStatusEqualTo(6);
        }
        else status=0;

        orders=orderMapper.selectByExample(orderExample);
        //获得 orderIds
        List<String> orderIds=new ArrayList<>();
        for(int i=0;i<orders.size();i++) {
//            System.out.println("订单号有："+orders.get(i).getOrderId());
//            System.out.println("字符串组size"+orderIds.size());
            List<OrderItem> orderItems;
            OrderItemExample orderItemExample=new OrderItemExample();
            OrderItemExample.Criteria criteria=orderItemExample.createCriteria();
            criteria.andOrderIdEqualTo(orders.get(i).getOrderId());
            criteria.andProductIdEqualTo(productId.toString());
            orderItems=orderItemMapper.selectByExample(orderItemExample);
            if(orderItems.size()>0) {
                for (OrderItem orderItem : orderItems) {
                    counts += orderItem.getNumber();
                }
            }
        }
        //获得状态为1 且 商品号为productId的订单项目

//        System.out.println(counts);
        return counts;
    }



    /**
     * 获得订单详细信息
     * @param orderId
     */
    @Override
    public Order getOrderInfo(String orderId) {
        List<Order> orderList=new ArrayList<Order>();
        OrderExample orderExample=new OrderExample();
        OrderExample.Criteria criteria=orderExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        orderList=orderMapper.selectByExample(orderExample);
//        System.out.println("orderId"+orderList.get(0).getOrderItemList().size());
//        System.out.println("商品价格："+orderList.get(0).getOrderItemList().get(0).getProductOrderItem().getPrice());
        return orderList.get(0);
    }

    @Override
    public Integer countOfOrders(Integer status) {
        OrderExample orderExample=new OrderExample();
        OrderExample.Criteria criteria=orderExample.createCriteria();
        criteria.andStatusEqualTo(status);
        Integer counts=orderMapper.countByExample(orderExample);
        return counts;
    }


    @Override
    public Integer countsAskReturnOrders() {
        OrderExample orderExample=new OrderExample();
        OrderExample.Criteria criteria=orderExample.createCriteria();
        criteria.andStatusEqualTo(4);
        Integer counts=orderMapper.countByExample(orderExample);
        return counts;
    }

    /**
     * 通过商品名找productId
     * @param name
     * @return
     */
    public String getProductIdByName(String name) {
        List<Product> products=new ArrayList<>();
        ProductExample example=new ProductExample();
        ProductExample.Criteria criteria1=example.createCriteria();
        criteria1.andProductNameEqualTo(name);
        System.out.println("找商品id----------------");
        products=productMapper.selectByExample(example);
        if(products.size()>0) {
            System.out.println("找到商品id");
            System.out.println(products.get(0).getProductId().toString());
            return products.get(0).getProductId().toString();
        }
        else {
            System.out.println("没找到商品id");
            return "";
        }
    }

    /**
     * 通过productId找orderId
     * @param name
     * @return
     */
    public List<OrderItem> getOrderIdByProductId(String name) {
        List<OrderItem> orderItems=new ArrayList<>();
        OrderItemExample orderItemExample=new OrderItemExample();
        OrderItemExample.Criteria criteria1=orderItemExample.createCriteria();
        criteria1.andProductIdEqualTo(getProductIdByName(name));
        orderItems=orderItemMapper.selectByExample(orderItemExample);
        if(orderItems.size()>0) {
            System.out.println("找到订单项目id");
            return orderItems;
        }
        else {
            System.out.println("没找到订单项目id");
            return orderItems;
        }
    }

    /**
     * 通过用户名查找用户Id
     * @param userName 用户名
     * @return
     */
    public String getUserIdByUserName(String userName) {
        List<User> users=new ArrayList<>();
        UserExample userExample=new UserExample();
        UserExample.Criteria criteria=userExample.createCriteria();
        criteria.andUsernameEqualTo(userName);
        users=userMapper.selectByExample(userExample);
        if(users.size()>0) {
            System.out.println("根据用户名查找到用户id");
            return users.get(0).getUserId();
        }else {
            System.out.println("根据用户名找不到用户id");
            return "";
        }
    }
}
