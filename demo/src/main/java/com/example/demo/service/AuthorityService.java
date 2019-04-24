package com.example.demo.service;


import com.example.demo.model.Authority;

/**
 * @author : chpyue@foxmail.com
 * @date :2018/11/5 18:40
 * @Description: Authority 服务接口
 */
public interface AuthorityService {
    /**
     * 根据id获取 Authority
     * @param authorityId
     * @return
     */
    Authority getAuthorityById(Integer authorityId);
    Authority getAuthorityByName(String name);
}
