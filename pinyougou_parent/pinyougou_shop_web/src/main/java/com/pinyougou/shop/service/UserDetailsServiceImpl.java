package com.pinyougou.shop.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户验证登录类,
 *
 * 认证类
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<GrantedAuthority> roles=new ArrayList<>();
        roles.add(this.getAuthority());
        return new User(username,"123",roles);
    }


    //定义一个觉得类

    public GrantedAuthority getAuthority(){
        GrantedAuthority auto = new SimpleGrantedAuthority("ROLE_SELLER");

        return auto;
    }

}
