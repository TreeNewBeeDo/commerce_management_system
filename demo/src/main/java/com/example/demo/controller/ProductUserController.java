package com.example.demo.controller;

import com.example.demo.service.ProductUserService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@EnableGlobalMethodSecurity(prePostEnabled = true)

@RequestMapping("/product")
public class ProductUserController {

    @Autowired
    private ProductUserService productUserService;
    @Autowired
    private UserService userService;

    /**
     * 商品种类表
     * @param model
     * @return
     */
    @RequestMapping("/categoryList")//list商品种类列表
    public ModelAndView categoryList(Model model){
        model.addAttribute("categoryList",productUserService.categoryList());
        return new ModelAndView("product/productlist","categoryModel",model);
    }

    /**
     * 全部商品按钮
     * @param model
     * @param orderbyname
     * @return
     */
    @RequestMapping("/allProduct")//所有商品页面
    public ModelAndView allProductList(Model model,String orderbyname,Integer kind) {
        if(kind!=null) {
            if (kind == -1) {
                kind = null;
            }
        }
        model.addAttribute("productList",productUserService.findByKind(kind,orderbyname));
        model.addAttribute("categoryList",productUserService.categoryList());
        try{
            model.addAttribute("user",userService.getUser());
        }catch (Exception e){
            System.out.println("暂未无用户登录");
        }
        model.addAttribute("selectType",orderbyname);
        model.addAttribute("isHidden",true);
        return new ModelAndView("product/productlist","productModel",model);
    }

    /**
     * 搜索框
     * @param model
     * @param name
     * @param
     * @return
     */
    @RequestMapping("/findProduct")//查找商品
    public ModelAndView findProduct(Model model,String name){
        model.addAttribute("productList",productUserService.findByName(name));
        try{
            model.addAttribute("user",userService.getUser());
        }catch (Exception e){
            System.out.println("暂未无用户登录");
        }
        model.addAttribute("categoryList",productUserService.categoryList());
        model.addAttribute("isHidden",false);
        return new ModelAndView("product/productlist","productModel",model);
    }


    /**
     * 种类按钮
     * @param model
     * @param kind
     * @param orderbyname
     * @return
     */
    @RequestMapping("/findKindProduct")//按种类显示商品
    public ModelAndView findKindProduct(Model model,Integer kind, String orderbyname){
        if(kind!=null) {
            if (kind == -1) {
                kind = null;
            }
        }
        model.addAttribute("productList",productUserService.findByKind(kind, orderbyname));
        model.addAttribute("selectType",orderbyname);
        model.addAttribute("categoryList",productUserService.categoryList());
        model.addAttribute("isHidden",true);
        model.addAttribute("kind",kind);
        try{
            model.addAttribute("user",userService.getUser());
        }catch (Exception e){
            System.out.println("暂未无用户登录");
        }
        return new ModelAndView("product/productlist","productModel",model);
    }

    /**
     * 商品详情页面
     * @param model
     * @param productId
     * @return
     */
    @RequestMapping("/productInfo")//单个商品
    public ModelAndView productInfo(Model model,int productId){
        model.addAttribute("productInfo",productUserService.productInfo(productId));
        model.addAttribute("categoryList",productUserService.categoryList());
        try{
            model.addAttribute("user",userService.getUser());
        }catch (Exception e){
            System.out.println("暂未无用户登录");
        }
        System.out.println("商品ID="+productId);
        return new ModelAndView("product/productinfo","productModel",model);
    }
}
