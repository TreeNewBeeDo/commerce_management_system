package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.ProductView;
import com.example.demo.model.User;
import com.example.demo.service.AdminOrderService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 商品管理
 *
 */
@Controller
@RequestMapping("/product")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminOrderService adminOrderService;


    /**
     * 商品列表
     * @param model
     * @return
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getProductList(Model model){
        List<ProductView> productList=productService.getProductList();
        model.addAttribute("productList",productList);
        model.addAttribute("user",userService.getUser());
        return new ModelAndView("product/Product");
    }

    /**
     * 商品详情
     * @param model
     * @param productId
     * @return
     */
    @RequestMapping("/detailpage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView findDetailByProductId(Model model,Integer productId){
        Integer sales=0;
        Integer sales1=adminOrderService.countsOrderProductByStatus(2,productId);
        Integer sales2=adminOrderService.countsOrderProductByStatus(3,productId);
        Integer sales3=adminOrderService.countsOrderProductByStatus(4,productId);
        Integer sales5=adminOrderService.countsOrderProductByStatus(6,productId);
        System.out.println(sales1+"  "+sales2+"  "+sales3+"  "+sales5);
        sales=sales1+sales2+sales3+sales5;
        model.addAttribute("sales",sales);
        model.addAttribute("productview",productService.findByProductId(productId));
        model.addAttribute("user",userService.getUser());
        return new ModelAndView("product/ProductDetails");
    }



    /**
     * 添加商品
     * @param product
     * @param flag 1.跳转添加页面 2.执行添加操作
     * @param
     * @return
     */
    @RequestMapping("/addproduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addProduct(MultipartFile file,Product product, String flag, Model model) throws IOException {
        List<Category> categoryList=categoryService.getCategoryList();
        model.addAttribute("user",userService.getUser());
        model.addAttribute("categoryList",categoryList);
        product.setDate(new Date());
        if(flag.equals("1")){
            return new ModelAndView("product/showAddProduct","productModel",model);
        }else {
            product.setImage(FileUtil.saveFile(file,"product"));
            productService.addProduct(product);
            return new ModelAndView("redirect:/product/list");
        }

    }

    /**
     * 修改商品信息
     * @param flag 1.跳转修改页面 2.执行修改操作
     * @param mv
     * @param product 修改的对象
     * @return
     */
    @RequestMapping("/modifiproduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView modifiProduct(MultipartFile file,String flag, Product product ,Model model)throws IOException{
        List<Category> categoryList=categoryService.getCategoryList();
        model.addAttribute("user",userService.getUser());
        model.addAttribute("categoryList",categoryList);
        if (flag.equals("1")){
            ProductView productView=productService.findByProductId(product.getProductId());
            model.addAttribute("productView",productView);
            return new ModelAndView("product/showModifiProduct","productmodel",model);
        }else {
            product.setImage(FileUtil.saveFile(file,"product"));
            productService.modifiProduct(product);
            return new ModelAndView("redirect:/product/list");
        }
    }

    /**
     * 根据id 删除商品
     * @param productId
     * @param
     * @return
     */
    @RequestMapping("/deleteproduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView removeProduct(Integer productId,Model model){
        Product product=productService.findProductById(productId);
        product.setIsEffective(0);
        productService.modifiProduct(product);
        return new ModelAndView("redirect:/product/list");
    }
}
