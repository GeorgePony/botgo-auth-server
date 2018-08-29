package com.originaltek.botgo.user.service;

import com.originaltek.botgo.user.util.MD5Util;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * create_time : 2018/8/22 17:40
 * author      : chen.zhangchao
 * todo        :
 */
public class AuthDaoAuthenticationProvider extends DaoAuthenticationProvider {




    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String presentedPassword = authentication.getCredentials().toString();
        String loginPassword = userDetails.getPassword();
        String encodePass = MD5Util.MD5SaltEncode(presentedPassword ).toString();
        if(!loginPassword.equals(encodePass)){
            throw new BadCredentialsException("用户名或密码错误");
        }
        //SecurityContextHolder.getContext().setAuthentication(authentication);
    }



}
