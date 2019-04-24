package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {

    private static final Integer serialVersionUID = 1;

    private Integer authorityId;

    private String name;

    public Integer getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Integer authorityId) {
        this.authorityId = authorityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    @Override
    public String getAuthority() {
        return name;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "authorityId=" + authorityId +
                ", name='" + name + '\'' +
                '}';
    }
}