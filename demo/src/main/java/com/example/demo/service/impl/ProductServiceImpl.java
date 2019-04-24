package com.example.demo.service.impl;

import com.example.demo.mapper.ProductMapper;
import com.example.demo.mapper.ProductViewMapper;
import com.example.demo.model.*;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 *
 * 商品的实现类
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductViewMapper productViewMapper;
    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductViewMapper productViewMapper, ProductMapper productMapper) {

        this.productViewMapper = productViewMapper;
        this.productMapper = productMapper;
    }

    /**
     *  获取所有商品信息集合
     * @return
     */
    @Autowired
    public List<ProductView> getProductList(){
        ProductViewExample example=new ProductViewExample();
        ProductViewExample.Criteria criteria=example.createCriteria();
        criteria.andIsEffectiveEqualTo(1);
        List<ProductView> productList=productViewMapper.selectByExample(example);
        return productList;

    }

    /**
     * 通过商品id查询商品
     * @param productId
     * @return
     */
    @Override
    public ProductView findByProductId(Integer productId) {
        ProductViewExample example=new ProductViewExample();
        ProductViewExample.Criteria criteria=example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        List<ProductView> productList=productViewMapper.selectByExample(example);
        if(productList.size()!=0){
            ProductView productView=productList.get(0);
            return productView;
        }else {
            return null;
        }
    }


    /**
     * 添加商品
     * @param product
     * @return
     */
    @Override
    public Product addProduct(Product product) {
        ProductExample example=new ProductExample();
//        ProductExample.Criteria criteria=example.createCriteria();
//        criteria.andIsEffectiveEqualTo(1);
        productMapper.insertSelective(product);
        return product;
    }

    /**
     * 通过id删除商品
     * @param productId
     */
    @Override
    public void removeProductById(Integer productId) {
        ProductViewExample example=new ProductViewExample();
        productMapper.deleteByPrimaryKey(productId);
    }

    /**
     * 修改商品信息
     * @param product
     */
    @Override
    public void modifiProduct(Product product) {
        ProductExample example=new ProductExample();
        productMapper.updateByPrimaryKeySelective(product);

    }

    @Override
    public ProductView getProudctView(Integer productId) {
        ProductViewExample example=new ProductViewExample();
        ProductViewExample.Criteria criteria=example.createCriteria();
        criteria.andProductIdEqualTo(productId);
        productViewMapper.selectByExample(example);
        return null;
    }

    /**
     * 根据id找商品
     * @param productId
     * @return
     */
    @Override
    public Product findProductById(Integer productId) {

        return productMapper.selectByPrimaryKey(productId);
    }
}
