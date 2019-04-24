package com.example.demo.service.impl;

import com.example.demo.mapper.AuthorityMapper;
import com.example.demo.model.Authority;
import com.example.demo.model.AuthorityExample;
import com.example.demo.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : chpyue@foxmail.com
 * @date :2018/11/5 18:44
 * @Description:
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityMapper authorityMapper;

    @Override
    public Authority getAuthorityById(Integer authorityId){
//        AuthorityExample example = new AuthorityExample();
//        AuthorityExample.Criteria criteria = example.createCriteria();
//        criteria.andAuthorityIdEqualTo(authorityId);
        return authorityMapper.selectByPrimaryKey(authorityId);
    }

    @Override
    public Authority getAuthorityByName(String name) {
        AuthorityExample example = new AuthorityExample();
        AuthorityExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        authorityMapper.selectByExample(example);
        List<Authority> authorityList = authorityMapper.selectByExample(example);
        if (authorityList.size()!=0){
            return authorityList.get(0);
        }
        else {
            return null;
        }

    }
}
