package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductView;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品的接口类
 *
 */
@Component
public interface ProductService {
    /**
     * 获取商品列表
     * @return
     */
    List<ProductView> getProductList();

    /**
     * 通过id查询商品
     * @param productId
     * @return
     */
    ProductView findByProductId(Integer productId);

    /**
     * 新增一个商品
     * @param product
     * @return
     */
    Product addProduct(Product product);

    /**
     * 通过id删除商品
     * @param productId
     */
    void removeProductById(Integer productId);

    /**
     * 修改商品信息
     * @param product
     */
    void modifiProduct(Product product);

    /**
     * 获取商品信息
     * @param productId
     * @return
     */
    ProductView getProudctView(Integer productId);



    /**
     * 根据id找商品
     * @param productId
     * @return
     */
    Product findProductById(Integer productId);
}
