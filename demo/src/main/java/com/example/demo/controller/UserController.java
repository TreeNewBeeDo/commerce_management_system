package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthorityService;
import com.example.demo.service.ProductUserService;
import com.example.demo.service.UserService;
import com.example.demo.utils.DateToString;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.MD5Util;
import com.example.demo.utils.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.controller
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yuedashadiao
 * @CreateDate: 2018-12-19 13:33
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2018-12-19 13:33
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Controller
//开启权限认证
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequestMapping(value = "/user")
public class UserController {

    private static final Integer ROLE_USER_AUTHORITY_ID = 2;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private ProductUserService productUserService;

    /**
     * 获取用户列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getUserList(Model model){
        List<User> userList = userService.getUserList();
        model.addAttribute("title","用户列表");
        model.addAttribute("userList",userList);
        //获取当前用户信息
        model.addAttribute("user",userService.getUser());

        return new ModelAndView("admin/userList","userModel",model);
    }

    /**
     * 获取管理员列表
     * @param model
     * @return
     */
    @GetMapping("/adminlist")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getAdminList(Model model){
        List<User> adminList = userService.getAdminList();
        model.addAttribute("title","用户列表");
        model.addAttribute("adminList",adminList);
        //获取当前用户信息
        model.addAttribute("user",userService.getUser());

        return new ModelAndView("admin/adminList","userModel",model);
    }


    /**
     * 通过userId查找对应用户信息（管理员）
     * @param userId
     * @param model
     * @return
     */
    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView getUserInfo(@PathVariable("userId") String userId, Model model){

        //所查询用户信息
        model.addAttribute("users",userService.findByUserId(userId));
        //获取当前用户信息
        model.addAttribute("user",userService.getUser());
        //日期格式化对象
        model.addAttribute("ds", new DateToString());
        return new ModelAndView("admin/profile","userModel",model);
    }

    /**
     * 跳转至当前用户详情信息（用户部分）
     * @param model
     * @return
     */
    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView userInfo(Model model){
        User user = userService.getUser();
        model.addAttribute("user",user);
        model.addAttribute("categoryList",productUserService.categoryList());
        //日期格式化对象
        model.addAttribute("ds", new DateToString());
        return new ModelAndView("/user/userInfo");
    }

    @GetMapping("/userEdit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView userEdit(Model model){
        User user = userService.getUser();
        model.addAttribute("user",user);
        model.addAttribute("categoryList",productUserService.categoryList());
        //日期格式化对象
        model.addAttribute("ds", new DateToString());
        return new ModelAndView("/user/userEdit");
    }

    /**
     * 跳转至用户个人信息修改页面（管理员）
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/toUserEdit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView toUserEdit(String userId,Model model){
        //所查询用户信息
        model.addAttribute("users",userService.findByUserId(userId));
        //获取当前用户信息
        model.addAttribute("user",userService.getUser());
        //日期格式化对象
        model.addAttribute("ds", new DateToString());
        return new ModelAndView("admin/profileEdit","userModel",model);
    }

    /**
     * 修改个人头像（管理员）
     * （仅允许修改本人）
     * @param file
     * @return
     */
    @PostMapping("/updateImage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateImage(MultipartFile file) throws IOException {
        User user = userService.getUser();
        user.setPortraitUrl(FileUtil.saveFile(file,"portrait"));
        userService.updateUser(user);
        return "redirect:/user/"+user.getUserId();
    }
    /**
     * 修改个人头像（用户）
     * （仅允许修改本人）
     * @param file
     * @return
     */
    @PostMapping("/updateImage0")
    @PreAuthorize("isAuthenticated()")
    public String updateImage0(MultipartFile file) throws IOException {
        User user = userService.getUser();
        user.setPortraitUrl(FileUtil.saveFile(file,"portrait"));
        userService.updateUser(user);
        return "redirect:/user/user";
    }

    /**
     * 更新用户信息(管理员）
     * @param user
     * @return
     */
    @RequestMapping("/updateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUser(User user){
        System.out.println("用户信息="+user.toString());
        userService.updateUser(user);
        if(user.getUsername() == null){
            return "redirect:/user/list";
        }
        return "redirect:/user/"+user.getUserId();
    }

    /**
     * 更新用户信息（用户）
     * @param user
     * @return
     */
    @PostMapping("/updateUsers")
    @PreAuthorize("isAuthenticated()")
    public String updateUsers(User user){
        System.out.println("用户信息="+user.toString());
        userService.updateUser(user);
        return "redirect:/user/user";
    }


    @PostMapping("/changePassword")
    @PreAuthorize("isAuthenticated()")
    public String changePassword(User user,HttpServletRequest request){
        String oldPassword = user.getPassword();
        String newPassword = request.getParameter("newPassword");
        String reNewPassword = request.getParameter("reNewPassword");

        System.out.println(newPassword + reNewPassword);
        User oldUser = userService.getUser();
        System.out.println("olduser:"+user.getPassword());
        System.out.println(oldUser.toString());
        if(MD5Util.encode(oldPassword).equals(oldUser.getPassword())){
            if(newPassword.equals(reNewPassword)){
                user.setPassword(MD5Util.encode(newPassword));
                userService.updateUser(user);
                System.out.println("更新成功");
            }else {
                System.out.println("两次输入的密码不一致");
            }
        }else {
            System.out.println("密码错误");
        }
        return "redirect:/logout";

    }

    /**
     * 校验密码是否正确
     * @param user
     * @return
     */
    @PostMapping("/checkOldPassword")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Msg checkOldPassword(User user) {
        User user1=userService.getUser();
        System.out.println("数据库："+user1.getPassword());
        System.out.println("输入："+user.getPassword());
        if(MD5Util.encode(user.getPassword()).equals(user1.getPassword())) {
            return Msg.success().add("checkResult","密码正确");
        }else {
            return Msg.fail().add("checkResult","密码错误");
        }
    }

    @PostMapping("/checkUsername")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Msg checkUsername(User user) {
        System.out.println("用户名："+user.getUsername());
        User user1=userService.findByUsername(user.getUsername());
        if(null!=user1) {
            return Msg.fail().add("checkResult","用户名已存在");

        } else{
            return Msg.success();
        }

    }


 }
