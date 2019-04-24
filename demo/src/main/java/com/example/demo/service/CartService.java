package com.example.demo.service;

import com.example.demo.model.Cart;


import java.util.List;

/**
 * Created by ilcy on 2018/12/24.
 */

public interface CartService {
    /**
     * 添加单个商品到购物车
     * @param cart
     */
    void insert(Cart cart);


    /**
     * 获取所有商品购物车
     * @return
     */
    List<Cart> getAllCarts(String user_id);



    /**
     * 根据cart_id获取单个商品购物车
     * @param cart_id
     * @return
     */
    Cart getSingleCart(String cart_id);



    /**
     * 修改单个商品购物车
     * @param cart
     */
    void updateCart(Cart cart);




    /**
     * 删除单个商品购物车
     * @param cartId
     */
    void deleteCart(String cartId);


    /**石诗佳
     * 通过id查询购物车
     * @param cartId
     * @return
     */
    Cart findCartById(String cartId);

    /**石诗佳
     * 通过id删除购物车
     * @param cartId
     * @return
     */
    int deleteCartById(String cartId);


    /**石诗佳
     * 通过id更新购物车
     */
    int updateCartById(Cart cart);


    /**石诗佳
     * 通过ids,得到cartList
     */
    List<Cart> findCartListByIds(String ids);

    /**石诗佳
     * 通过ids，得到订单总价
     */
    Double getTotalPrice(String ids);

}
