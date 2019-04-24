package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

/**
 *
 * 商品种类
 */
@Controller
@RequestMapping("/category")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CategoryController {


    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    /**
     * 查询商品种类信息
     * @param model
     * @return
     */
    @RequestMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getCategoryList(Model model){
//        System.out.println("---");
        List<Category> categoryList=categoryService.getCategoryList();
        System.out.println(categoryList.size());
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("user",userService.getUser());
        return new ModelAndView("category/Category");

    }

    /**
     * 添加新的种类
     * @param flag 1.跳转添加页面 2.执行添加操作
     * @param model
     * @param category 要添加的对象
     * @return
     */
    @RequestMapping("/addcategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCategory(String flag,Model model, Category category){
        model.addAttribute("user",userService.getUser());
        if (flag.equals("1")){
            return new ModelAndView("category/showAddCategory");
        }else {
            categoryService.addCategory(category);
            return new ModelAndView("redirect:/category/list");
        }
    }

    /**
     * 删除种类
     * @param categoryId 通过种类id删除
     * @param mv
     * @return
     */
    @RequestMapping("/deletecategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView removeCategory(Integer categoryId,ModelAndView mv){
//        System.out.println(categoryId);
        categoryService.removeCategoryById(categoryId);
        mv.setViewName("redirect:/category/list");
        return mv;
    }

    /**
     * 修改种类信息
     * @param flag 1.跳转修改页面 2.执行修改操作
     * @param model
     * @param category 修改的对象
     * @return
     */
    @RequestMapping("/modificategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView modifiCategroy(String flag,Model model, Category category){
        model.addAttribute("user",userService.getUser());
        if (flag.equals("1")){
            //获取目标对象
            Category category1=categoryService.findByCategoryId(category.getCategoryId());
            model.addAttribute("category",category1);
            return new ModelAndView("category/showModifiCategory");
        }else {
            categoryService.modifiCategory(category);
            return new ModelAndView("redirect:/category/list");
        }
    }
}
