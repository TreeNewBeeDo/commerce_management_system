package com.example.demo.controller;




import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.custom.CartCustom;

import com.example.demo.service.CartService;

import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;


import java.text.DecimalFormat;


/**
 * Created by ilcy on 2018/12/24.
 */


@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping(value = "/cart")
public class CartController {


    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    /**
     * 插入单个商品购物车
     * @param productId
     */
    @GetMapping("/insert")
    public String insert(String productId,String productCount){
        List<Cart> list=cartService.getAllCarts(userService.getUser().getUserId());
        //遍历集合
        for( int i=0;i<=list.size();i++){
            //遍历完集合没有符合商品，执行添加新的购物车条目
            if (i>=list.size()){
                Cart cart=new Cart();
                cart.setCount(Double.parseDouble(productCount));
                cart.setCartId(UUIDUtil.getUUID());
                cart.setUserId(userService.getUser().getUserId());
                cart.setProductId(Integer.parseInt(productId));
                cartService.insert(cart);
                break;
            }
            //判断该用户所拥有的购物车是否已有该商品，若有即更新数量
            Cart c=list.get(i);
            if (Integer.parseInt(productId)==c.getProductId()){
                c.setCount(c.getCount()+Integer.parseInt(productCount));
                cartService.updateCart(c);
                break;
            }
        }
        return "redirect:/product/productInfo?productId="+Integer.parseInt(productId);

    }



    /**
     * 显示我的购物车
     * @param model
     * @return
     */
    @GetMapping(value = "/list")
    public ModelAndView getCartList( Model model){
        Double sum=0.00;
        List<Cart> list=cartService.getAllCarts(userService.getUser().getUserId());
        ArrayList<CartCustom> cartCustomList=new ArrayList<>();
        //遍历购物车，根据商品id获取商品对象
        for (Cart cart:list) {
            CartCustom cartCustom=new CartCustom();
            Product product= productService.findProductById(cart.getProductId());
            cartCustom.setProductName(product.getProductName());
            cartCustom.setProductPrice(product.getPrice());
            cartCustom.setImgUrl(product.getImage());
            cartCustom.setProductCount(cart.getCount());
            sum=sum+(product.getPrice())*(cart.getCount());
            cartCustom.setCartId(cart.getCartId());
            cartCustomList.add(cartCustom);
        }
        model.addAttribute("cartCustomList",cartCustomList);
        model.addAttribute("summary",new DecimalFormat("#.00").format(sum));
        model.addAttribute("user",userService.getUser());
        return  new ModelAndView("cart/ct","cartModel",model);
    }




    /**
     * 删除单个商品购物车条目
     * @param cartId
     */
    @GetMapping(value = "/delete")
   public ModelAndView deleteSingleCart(String cartId,ModelAndView mv){
        cartService.deleteCart(cartId);
        String userId=userService.getUser().getUserId();
        mv.addObject("userId",userId);
        mv.setViewName("redirect:/cart/list");
        return mv;
    }



    /**
     * 批量删除商品条目
     * @param ids
     * @return
     */
    @GetMapping(value = "/removeCarts")
    public ModelAndView deleteAllCarts(String ids,ModelAndView mv){
        String[] arr=ids.split(",");
        for (String id:arr
        ) {
            cartService.deleteCart(id);
        }
        String userId=userService.getUser().getUserId();
        mv.addObject("userId",userId);
        mv.setViewName("redirect:/cart/list");
        return mv;
   }




    /**
     * 修改购物车数量
     * @param productCount
     * @param cartId
     */
   @PostMapping(value = "/update/count")
   @ResponseBody
   public Double updateCount(String productCount,String cartId){
       Double s2=0.00;
       Double num= Double.parseDouble(productCount);
       Cart cart=cartService.getSingleCart(cartId);
       cart.setCount(num);
       cartService.updateCart(cart);
       List<Cart> list=cartService.getAllCarts(userService.getUser().getUserId());
       for (Cart c:list
       ) {
           Product product= productService.findProductById(c.getProductId());
           s2=s2+(c.getCount())*(product.getPrice());
       }
       return s2;
   }

}
