package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.service
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yueChunPeng
 * @CreateDate: 2018-12-19 13:32
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2018-12-19 13:32
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Component
public interface UserService extends UserDetailsService {
//    List<User> getUserList();
//    User saveUser(User user);

    /**
     * 获取整个用户信息
     * @return
     */
    User getUser();

    /**
     * 通过用户名称获取用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 通过用户ID查询对应用户
     * @param userId
     * @return
     */
    User findByUserId(String userId);

    /**
     * 获取全部用户信息列表
     * @return
     */
    List<User> getUserList();

    /**
     * 获取管理员列表
     * @return
     */
    List<User> getAdminList();

    /**
     * 获取超级管理员
     * @return
     */
    List<User> getRootList();

    /**
     * 禁止调用
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 插入一条用户信息
     * @param user
     * @return
     */
    User insertUser(User user);


    /**
     * 更新一条用户信息
     * @param user
     */
    void updateUser(User user);


}
