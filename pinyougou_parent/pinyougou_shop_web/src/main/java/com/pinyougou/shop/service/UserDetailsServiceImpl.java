package com.pinyougou.shop.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户验证登录类,
 *
 * 认证类
 */

public class UserDetailsServiceImpl implements UserDetailsService {


    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1通过username查询数据库中的是否包含这个用户的信息
        TbSeller seller = sellerService.findOne(username);
        //2判断seller是否存在如果不存在则返回null
        if (seller==null){
            return null;
        }
        //3 如果seller存在,判读status是否为1,如果不是1返回null
        if (!"1".equals(seller.getStatus())){
            return null;
        }
        // 4把seller的用户名和密码赋值给user,并返回

        List<GrantedAuthority> roles=new ArrayList<>();
        roles.add(this.getAuthority());
        return new User(username,seller.getPassword(),roles);
    }


    //定义一个添加权限的类
    public GrantedAuthority getAuthority(){
        GrantedAuthority auto = new SimpleGrantedAuthority("ROLE_SELLER");

        return auto;
    }

}
