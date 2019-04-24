package com.example.demo.service;


import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品种类的接口
 */
@Component
public interface CategoryService{
    /**
     * 暂时没用就没写
     * 获取所有商品种类
     *
     * @return
     */
    Category getCategory();

    /**
     * 获取全部种类信息列表
     * @return
     */
    List<Category> getCategoryList();
    /**
     * 通过种类id查询
     *
     * @return
     */
    Category findByCategoryId(Integer categoryId);

    /**
     * 新增一个种类
     * @param category
     * @return
     */
    Category addCategory(Category category);

    /**
     * 通过id删除种类
     * @param categoryId
     */
    void removeCategoryById(Integer categoryId);

    /**
     * 修改种类信息
     * @param category
     */
    void modifiCategory(Category category);

    /**
     * 获取该种类下的所有商品信息
     * @param categoryId
     * @return
     */
    List<Product> getProductByCategoryId(Integer categoryId);

}
