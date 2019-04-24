package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.service.*;
import com.example.demo.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
/**
 * @Auther 石诗佳
 * @Date 2019/1/3 17 01
 * @Desription
 */
public class OrderController {

    private static final Integer ROLE_USER_AUTHORITY_ID = 2;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";


    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductUserService productUserService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;


    /**
     * 选中购物车，进入结算
     *
     * @param ids
     * @param model
     * @return productAndOrderItems  totalPrice
     */
    @GetMapping("/buy")
    public ModelAndView buy(String ids, Model model) {
        List<Cart> carts = cartService.findCartListByIds(ids);
        System.out.println("size=" + carts.size());
        Double totalPrice = 0d;
        List<ProductOrderItem> productOrderItems = new ArrayList<>();
        for (Cart cart : carts) {

            ProductOrderItem productOrderItem = new ProductOrderItem();
            Product product = productUserService.productInfo(cart.getProductId());
            productOrderItem.setProductName(product.getProductName());
            productOrderItem.setImage(product.getImage());
            productOrderItem.setNumber(cart.getCount().intValue());//double类型转Integer
            productOrderItem.setProductId(cart.getProductId());
            productOrderItem.setCartId(cart.getCartId());
            productOrderItem.setPrice(product.getPrice());
            totalPrice += productOrderItem.getTotalPrice();
            productOrderItems.add(productOrderItem);
        }
        User user = userService.getUser();
        String address=user.getAddress();
        System.out.println(productOrderItems.toString());
        model.addAttribute("productOrderItems", productOrderItems);//订单详情条目展示
        model.addAttribute("totalPrice", totalPrice);//总价
        model.addAttribute("address",address);//地址
        model.addAttribute("user",userService.getUser());
        System.out.println("进入结算中心");
        //跳转到生成订单页面（输入收货信息）
        return new ModelAndView("order/addOrder1", "buyModel", model);
    }

    @GetMapping("/buyProduct")
    public ModelAndView buyProduct(Model model,String productId, Integer num){

        ProductOrderItem productOrderItem = new ProductOrderItem();
        Product product = productUserService.productInfo(Integer.parseInt(productId));
        productOrderItem.setProductName(product.getProductName());
        productOrderItem.setImage(product.getImage());
        productOrderItem.setNumber(num);
        productOrderItem.setProductId(Integer.parseInt(productId));
        productOrderItem.setPrice(product.getPrice());
        Double totalPrice=productOrderItem.getTotalPrice();

        User user = userService.getUser();
        String address=user.getAddress();
        model.addAttribute("productOrderItem", productOrderItem);//订单详情条目展示
        model.addAttribute("totalPrice", totalPrice);//总价
        model.addAttribute("address",address);//地址
        model.addAttribute("user",userService.getUser());
        System.out.println("进入结算中心");
        //跳转到生成订单页面（输入收货信息）
        return new ModelAndView("order/addOrderByProduct", "buyModel", model);


    }

    /**
     * 直接通过商品提交订单
     *
     * @param order
     * @param productId
     * @param number
     * @param totalPrice
     * @return
     */
    @PostMapping("/addOrderByProduct")
     public ModelAndView addOrderByProduct(Model model,Order order, String productId, Integer number, Double totalPrice){
        System.out.println(order.toString());
        System.out.println(productId);
        System.out.println(number);
        System.out.println(totalPrice);
        User user = userService.getUser();
        order.setOrderId(UUIDUtil.getUUID());
        order.setTotalPrice(new BigDecimal(totalPrice));
        order.setUserId(user.getUserId());
        order.setStatus(1);
        order.setOrderTime(new Date());

        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(UUIDUtil.getUUID());
        orderItem.setOrderId(order.getOrderId());
        orderItem.setNumber(number);
        Product product = productUserService.productInfo(Integer.valueOf(productId));//通过product_id，来得到product
        System.out.println("product="+product.toString());
        int count = product.getCount() - number;
        product.setCount(count);
        int sales = product.getSales() +number;
        product.setSales(sales);
        orderItem.setPerPrice(product.getPrice());//封装订单明细表中的单价
        orderItem.setProductId(productId);
        //修改商品表的库存量和销量
        int a1 = productUserService.updateProductById(product);
        //插入orderItem表
        int c = orderItemService.insertOrderItem(orderItem);
        //a b c小于0，怎么报错
        if (0 >= a1 || 0 >= c || 0 >= c ) {
            System.out.println("生成订单出错");
        }

    int d = orderService.insertOrder(order);
        model.addAttribute("order",order);
        model.addAttribute("user",userService.getUser());
    return new ModelAndView("order/addOrderSuccess","model",model);
}

    /**
     * 通过购物车提交订单
     * @param model
     * @param order
     * @param productIds
     * @param numbers
     * @param cartIds
     * @param totalPrice
     * @return
     */

    @PostMapping("/addOrder")
    public ModelAndView addOrder(Model model, Order order, String productIds, String numbers, String cartIds, Double totalPrice) {

        User user = userService.getUser();
        order.setOrderId(UUIDUtil.getUUID());
        order.setTotalPrice(new BigDecimal(totalPrice));
        order.setUserId(user.getUserId());
        order.setStatus(1);
        order.setOrderTime(new Date());
        System.out.println(productIds.length());
        String[] productIdArray = productIds.split(",");
        String[] numArray = numbers.split(",");
        String[] cartArray = cartIds.split(",");
        for (int i = 0; i < numArray.length; i++) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(UUIDUtil.getUUID());
            orderItem.setOrderId(order.getOrderId());
            orderItem.setNumber(Integer.valueOf(numArray[i]));
            Product product = productUserService.productInfo(Integer.valueOf(productIdArray[i]));//通过product_id，来得到product
            int count = (int) (product.getCount() - Integer.valueOf(numArray[i]));
            product.setCount(count);
            int sales = (int) (product.getSales() + Integer.valueOf(numArray[i]));
            product.setSales(sales);
            orderItem.setPerPrice(product.getPrice());//封装订单明细表中的单价
            orderItem.setProductId(productIdArray[i]);//productItem中product_id为Integer orderItem中product_id为String类型
            //修改商品表的库存量和销量
            int a1 = productUserService.updateProductById(product);
            //删除cart表的记录
            int b = cartService.deleteCartById(cartArray[i]);
            //插入orderItem表
            int c = orderItemService.insertOrderItem(orderItem);
            //a b c小于0，怎么报错
            if (0 >= a1 || 0 >= b || 0 >= c ) {
                System.out.println("生成订单出错");
            }
        }
        int d = orderService.insertOrder(order);
        model.addAttribute("order",order);
        model.addAttribute("user",userService.getUser());
        //跳转到下单成功页面
        return new ModelAndView("order/addOrderSuccess","model",model);
    }




    /**
     * 确认收货
     * @param model
     * @param orderId
     * @return
     */
    @GetMapping("/confirmOrder")
    public ModelAndView confirmOrder(Model model, String orderId) {
        Order order = orderService.findOrder(orderId);
        //封装收货时间
        order.setCompleteTime(new Date());
        order.setStatus(3);
        //修改收货时间和订单状态
        int a = orderService.confirmOrder(order);
        if (a < 0){
            System.out.println("确认收货出错");
        }

        //跳转到确认收货成功页面
        return new ModelAndView("order/confirmOrderSuccess");
    }


    /**
     * 进入申请退货页面
     * @param model
     * @param orderId
     * @return
     */
    @GetMapping("/applyReturn")
    public ModelAndView applyReturn(Model model, String orderId) {
        Order order = orderService.findOrder(orderId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("user",userService.getUser());
        //跳转到退货说明页面
        return new ModelAndView("order/applyReturn", "model", model);
    }


    /**
     * 已输入退货说明，申请退货
     * @param
     * @param orderId
     * @param returnReason
     * @return
     */
    @GetMapping("/returnOrder")
    public ModelAndView returnOrder( String orderId,String returnReason) {
        Order order=orderService.findOrder(orderId);
        order.setReturnReason(returnReason);
        order.setStatus(4);
        int a = orderService.confirmOrder(order);
        if (a < 0) {
            System.out.println("退货申请不成功");
        }
        //跳转到退货申请成功页面
        return new ModelAndView("order/returnOrderSuccess");
    }


    /**
     * 空页
     * @param model
     * @return
     */
    @GetMapping("/myOrder")
    public ModelAndView myOrder(Model model) {
        //跳转到我的订单页面
        return new ModelAndView("order/myOrder");
    }

    /**
     * 查询不同状态的订单
     *
     * @param model
     * @param flag  选择的分类 1已下单未发货  2已发货 3已收货 4退货未受理  5退货成功
     * @return
     */
    @GetMapping("/findOrders")
    public ModelAndView findOrders(Model model, String flag) {
        //获取当前用户
        User user = userService.getUser();
        List<Order> orders = orderService.findOrders(Integer.parseInt(flag), user.getUserId());
        //如果查询后没有订单，跳到提示页面
        if (orders.size() == 0)
            return new ModelAndView("order/zeroOrder");

        model.addAttribute("orders", orders);
        model.addAttribute("flag",flag);
        //跳转到选择状态的订单列表页面
        return new ModelAndView("order/differentOrders", "ordersModel", model);

    }


    /**
     * 点击订单表中view，进入订单详情页面
     *
     * @param model
     * @param orderId
     * @return
     */

    @GetMapping("/orderDetail")
    public ModelAndView orderDetail(Model model, String orderId,String flag) {
        Order order = orderService.findOrder(orderId);
        List<OrderItem> orderItems = order.getOrderItemList();
            System.out.println(orderItems.size());

        List<ProductOrderItem> productOrderItems = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            Product product = productUserService.productInfo(Integer.valueOf(orderItem.getProductId()));
            ProductOrderItem productOrderItem = new ProductOrderItem();
            productOrderItem.setItemId(orderItem.getItemId());
            productOrderItem.setPrice(orderItem.getPerPrice());
            productOrderItem.setNumber(orderItem.getNumber());
            productOrderItem.setProductName(product.getProductName());
            productOrderItem.setImage(product.getImage());
            productOrderItems.add(productOrderItem);
        }
        model.addAttribute("productOrderItems", productOrderItems);
        model.addAttribute("order", order);
        model.addAttribute("flag",flag);
        model.addAttribute("user",userService.getUser());
        model.addAttribute("categoryList",productUserService.categoryList());
        //跳转到选择状态的订单明细表页面
        return new ModelAndView("order/orderDetails", "orderItemsModel", model);

    }

    /**
     * 订单中心
     * @param model
     * @return
     */
    @GetMapping("/orderCenter")
    public ModelAndView orderCenter(Model model) {
//        model.addAttribute("productInfo",productUserService.productInfo(productId));
        model.addAttribute("categoryList",productUserService.categoryList());
        try{
            model.addAttribute("user",userService.getUser());
        }catch (Exception e){
            System.out.println("暂未无用户登录");
        }
//        System.out.println("商品ID="+productId);
        return new ModelAndView("order/orderCenter","productModel",model);
    }


/*    public ModelAndView addOrderByProduct(Model model,String productId) {
        //跳转到我的订单页面

        return new ModelAndView("order/addOrder1");
    }*/









}
