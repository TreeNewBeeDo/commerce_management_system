package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ProductView;
import com.example.demo.service.AdminOrderService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.utils.Msg;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Controller
public class AdminOrderController {

    @Autowired
    private AdminOrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    /**
     * 获得订单list
     * @param type
     * @param pn
     * @return 返回订单列表json数据
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/adminGetOrders/{type}")
    public Msg getOrder(@PathVariable("type") Integer type, @RequestParam(value="pn",defaultValue = "1")Integer pn) {
        PageHelper.startPage(pn,5);
        List<Order> orders=orderService.getOrderAndOrderItem(type);
        System.out.println("订单个数："+orders.size());
        PageInfo page = new PageInfo(orders,5);
        return Msg.success().add("pageInfo",page);
    }

    /**
     * 查找订单
     * @param seekContent 查找内容
     * @param seekType 查找类型
     * @return 返回json数据
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/adminSeekOrders")
    public String getOrder(Model model,@RequestParam("seekContent")String seekContent
            ,@RequestParam("seekType")Integer seekType) {
        List<Order> orderList= orderService.seekOrder(seekContent,seekType);
        model.addAttribute("orderList",orderList);
        model.addAttribute("user",userService.getUser());
        return "admin/order/seekOrder";
    }

    /**
     * 订单发货、同意退货、拒绝退货   编辑订单状态
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @PostMapping("/adminOrderShip")
    public Msg orderShip(Order order) {
        orderService.orderShip(order);
        return Msg.success();
    }

    /**
     * 路径跳转
     * @param path
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/adminOrder/{path}")
    public String toPath(@PathVariable String path,Model model) {
        model.addAttribute("user",userService.getUser());
        model.addAttribute("countsAskReturnOrder",orderService.countsAskReturnOrders());
        return "admin/order/"+path;
    }


    /**
     * 获得productName productImage
     * @param productId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/adminGetProductInfo")
    public Msg getProductInfo(@RequestParam("productId")Integer productId) {
        ProductView productView=productService.findByProductId(productId);
        System.out.println("后台订单获取商品信息");
        return Msg.success().add("productName",productView.getProductName())
                .add("productImage",productView.getImage());
    }

    /**
     * 获得userName
     * @param userId
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/adminGetUserInfo")
    public Msg getProductInfo(@RequestParam("userId")String userId) {
        String userName=userService.findByUserId(userId).getName();
        return Msg.success().add("userName",userName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/adminGetOrderInfo")
    public Msg getOrderInfoDesc(@RequestParam("orderId")String orderId) {
        Order order=orderService.getOrderInfo(orderId);
        return Msg.success().add("order",order);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/toOrderInfo")
    public String toOrderInfo(String orderId,Model model) {

        model.addAttribute("orderId",orderId);
        model.addAttribute("user",userService.getUser());
        System.out.println(orderId);
        return "admin/order/orderInfo";

    }




}
