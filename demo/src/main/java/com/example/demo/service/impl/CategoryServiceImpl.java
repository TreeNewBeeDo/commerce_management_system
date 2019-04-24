package com.example.demo.service.impl;

import com.example.demo.mapper.CategoryMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.model.Category;
import com.example.demo.model.CategoryExample;
import com.example.demo.model.Product;
import com.example.demo.model.ProductExample;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl  implements CategoryService{
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    @Autowired
    public CategoryServiceImpl(CategoryMapper categoryMapper, ProductMapper productMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
    }

    /**
     * 获取整个商品种类信息
     * @return
     * 好像没什么用这个
     */
    @Override
    public Category getCategory(){


        return null;
    }

    /**
     * 获取商品种类集合
     * @return
     */
    @Override
    public List<Category> getCategoryList() {
        CategoryExample example=new CategoryExample();
//        CategoryExample.Criteria criteria=example.createCriteria();
//        criteria.andCategoryIdIsNotNull();
        List<Category> categoryList= categoryMapper.selectByExample(example);
        return categoryList;
    }

    /**
     * 通过id查询商品种类
     * @param categoryId
     * @return
     */
    @Override
    public Category findByCategoryId(Integer categoryId) {
        CategoryExample example=new CategoryExample();
        CategoryExample.Criteria criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Category> categoryList= categoryMapper.selectByExample(example);
        if(categoryList.size()!=0){
            Category category=categoryList.get(0);
            return category;
        }else {
            return null;
        }

    }

    /**
     * 添加商品种类
     * @param category
     * @return
     */
    @Override
    public Category addCategory(Category category) {
        CategoryExample example=new CategoryExample();
        categoryMapper.insertSelective(category);
        return category;
    }

    /**
     * 根据id删除商品种类
     * 要求该分类下有商品不允许删除
     * 没有商品可以删除
     * @param categoryId
     */
    @Override
    public void removeCategoryById(Integer categoryId) {
        //获取该商品下的商品集合
       List<Product> productList=getProductByCategoryId(categoryId);
        if (productList.size()!=0){
            //带一句话到前端页面提示不可以删除
            //

        }else{
            CategoryExample example=new CategoryExample();
            categoryMapper.deleteByPrimaryKey(categoryId);
       }
    }

    /**
     * 修改商品种类信息
     * @param category
     */
    @Override
    public void modifiCategory(Category category) {
        CategoryExample example=new CategoryExample();
      //  CategoryExample.Criteria criteria=example.createCriteria();
        //criteria.andCategoryIdEqualTo(catgeoryId);
        categoryMapper.updateByPrimaryKeySelective(category);

    }

    /**
     * 获取该种类下的商品集合
     * @param categoryId
     * @return
     */
    @Override
    public List<Product> getProductByCategoryId(Integer categoryId) {
        ProductExample example=new ProductExample();
        ProductExample.Criteria criteria=example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Product> productList=productMapper.selectByExample(example);
        return productList;
    }

}
