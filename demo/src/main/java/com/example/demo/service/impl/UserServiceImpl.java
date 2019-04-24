package com.example.demo.service.impl;

import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.mapper.UserAuthorityMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.*;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: demo
 * @Package: com.example.demo.service.impl
 * @ClassName: ${TYPE_NAME}
 * @Description: java类作用描述
 * @Author: yueChunPeng
 * @CreateDate: 2018-12-19 13:32
 * @UpdateUser: Neil.Zhou
 * @UpdateDate: 2018-12-19 13:32
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Integer ROLE_ROOT_AUTHORITY_ID = 0;
    private static final Integer ROLE_ADMIN_AUTHORITY_ID = 1;
    private static final Integer ROLE_USER_AUTHORITY_ID = 2;
//    @Autowired
    private final UserMapper userMapper;
    private final UserAuthorityMapper userAuthorityMapper;
    private final AuthorityMapper authorityMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserAuthorityMapper userAuthorityMapper, AuthorityMapper authorityMapper) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.authorityMapper = authorityMapper;
    }

    /**
     * 通过用户名称获取对应用户信息
     * 【有权限信息】
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria= example.createCriteria();
        criteria.andUsernameEqualTo(username);

        List<User> userList = userMapper.selectByExample(example);

        if(userList.size()!=0){
            User user = userList.get(0);
            //中间表

            UserAuthorityExample example1 = new UserAuthorityExample();
            UserAuthorityExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andUserIdEqualTo(user.getUserId());

            List<UserAuthority> userAuthorityList = userAuthorityMapper.selectByExample(example1);

            //提取权限ID
            List<Integer> authotityIdList = new ArrayList<>();
            for (UserAuthority userAuthority : userAuthorityList) {
                authotityIdList.add(userAuthority.getAuthorityId());
            }
            //权限表
            List<Authority> authorities = new ArrayList<>();

            AuthorityExample example2 = new AuthorityExample();
            AuthorityExample.Criteria criteria2 = example2.createCriteria();
            criteria2.andAuthorityIdIn(authotityIdList);
            authorities = authorityMapper.selectByExample(example2);

            user.setAuthorities(authorities);

            return user;
        }
        else {
            return null;
        }
    }

    @Override
    public User findByUserId(String userId) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();

        criteria.andUserIdEqualTo(userId);

        List<User> userList = userMapper.selectByExample(example);

        if(userList.size()!=0){
            String username = userList.get(0).getUsername();
            return findByUsername(username);
        }
        else {
            return null;
        }
    }

    @Override
    public List<User> getUserList() {
//        获取userIdList
        UserAuthorityExample example = new UserAuthorityExample();
        UserAuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andAuthorityIdEqualTo(ROLE_USER_AUTHORITY_ID);
        List<UserAuthority> userAuthorityList = userAuthorityMapper.selectByExample(example);
        System.out.println("列表="+userAuthorityList.toString());
        List<User> userList = new ArrayList<>();
        for (UserAuthority userAuthority : userAuthorityList) {
            userList.add(findByUserId(userAuthority.getUserId()));
        }
        return userList;
    }

    @Override
    public List<User> getAdminList() {
        UserAuthorityExample example = new UserAuthorityExample();
        UserAuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andAuthorityIdEqualTo(ROLE_ADMIN_AUTHORITY_ID);
        List<UserAuthority> userAuthorityList = userAuthorityMapper.selectByExample(example);
        List<User> userList = new ArrayList<>();
        for (UserAuthority userAuthority : userAuthorityList) {
            userList.add(findByUserId(userAuthority.getUserId()));
        }
        return userList;
    }

    @Override
    public List<User> getRootList() {
        UserAuthorityExample example = new UserAuthorityExample();
        UserAuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andAuthorityIdEqualTo(ROLE_ROOT_AUTHORITY_ID);
        List<UserAuthority> userAuthorityList = userAuthorityMapper.selectByExample(example);
        List<User> userList = new ArrayList<>();
        for (UserAuthority userAuthority : userAuthorityList) {
            userList.add(findByUserId(userAuthority.getUserId()));
        }
        return userList;
    }

    /**
     * 通过用户名获取对应用户信息
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Authority authority : user.getAuthorityList()) {
            authorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);

    }

    /**
     * 插入用户信息
     * @param user
     * @return
     */
    @Override
    public User insertUser(User user) {
        UserExample userExample = new UserExample();
        userMapper.insertSelective(user);

        for (Authority authority : user.getAuthorityList()) {
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setUserId(user.getUserId());
            userAuthority.setAuthorityId(authority.getAuthorityId());
            userAuthorityMapper.insertSelective(userAuthority);
        }
        return user;
    }



    @Override
    public User getUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = findByUsername(username);

        return user;
    }
    @Override
    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

}
