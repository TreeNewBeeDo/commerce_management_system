package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductUserService {

    /**
     * 输出全部商品类型
     * @return
     */
    List<Category> categoryList();

    /**
     *按类型查找商品，并按照对应要求排序
     */
    List<Product> findByKind(Integer kind, String orderbyname);

    /**
     * 按名称查找商品（模糊查询），并按照对应要求排序
     */
    List<Product> findByName(String name);


    /**
     * 单件商品的信息
     * @param productId
     * @return
     */
    Product productInfo(int productId);

    /**
     * 石诗佳
     * 更新商品信息
     * @param product
     * @return
     */
    int updateProductById(Product product);

}
