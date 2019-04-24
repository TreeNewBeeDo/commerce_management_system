package com.example.demo.service.impl;

import com.example.demo.mapper.CartMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Cart;
import com.example.demo.model.CartExample;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilcy on 2018/12/24.
 */
@Service
public class CartServiceImpl implements CartService{


    private final CartMapper cartMapper;

    private final ProductMapper productMapper;



    @Autowired
    public CartServiceImpl(CartMapper cartMapper,ProductMapper productMapper){
        this.cartMapper=cartMapper;
        this.productMapper=productMapper;
    }

    /**
     * 单个商品条目添加到购物车
     * @param cart
     */
    @Override
    public void insert(Cart cart) {
        cartMapper.insert(cart);
    }


    /**
     * 根据用户id读取购物车条目
     * @param user_id
     * @return
     */
    @Override
    public List<Cart> getAllCarts(String user_id) {
        CartExample example=new CartExample();
        CartExample.Criteria criteria=example.createCriteria();
        criteria.andUserIdEqualTo(user_id);
        List<Cart> cartList=cartMapper.selectByExample(example);
        return cartList;
    }


    /**
     * 获取单个商品条目的购物车
     * @param cart_id
     * @return
     */
    @Override
    public Cart getSingleCart(String cart_id) {
        Cart cart=cartMapper.selectByPrimaryKey(cart_id);
        return cart;
    }


    /**
     *删除单个商品条目的购物车
     * @param cartId
     */
    @Override
    public void deleteCart(String cartId) {
        CartExample example=new CartExample();
        CartExample.Criteria criteria=example.createCriteria();
        criteria.andCartIdEqualTo(cartId);
        cartMapper.deleteByExample(example);
    }


    /**
     * 修改单个商品条目的购物车
     * @param cart
     */
    @Override
    public void updateCart(Cart cart) {cartMapper.updateByPrimaryKey(cart);}

    /**
     * 石诗佳
     * @param cartId
     * @return
     */
    @Override
    public  Cart findCartById(String cartId){
        return cartMapper.selectByPrimaryKey(cartId);
    }
    @Override
    public int deleteCartById(String cartId){
        return cartMapper.deleteByPrimaryKey(cartId);
    }

    @Override
    public int updateCartById(Cart cart){
        return cartMapper.updateByPrimaryKey(cart);
    }
    @Override
    public  List<Cart> findCartListByIds(String ids){
        String[] idArray=ids.split(",");
        List<Cart>carts=new ArrayList<>();

        //获取购物车集合
        for(String id:idArray){
            Cart cart=cartMapper.selectByPrimaryKey(id);
            carts.add(cart);
        }
        return  carts;
    }

    @Override
    public Double getTotalPrice(String ids){
        Double totalPrice=0d;
        String[] idArray=ids.split(",");
        for(String id:idArray){
            Cart cart=cartMapper.selectByPrimaryKey(id);
            Product product=productMapper.selectByPrimaryKey(cart.getProductId());
            double price=cart.getCount()*product.getPrice();
            totalPrice+=price;
        }
        return totalPrice;

    }


}
