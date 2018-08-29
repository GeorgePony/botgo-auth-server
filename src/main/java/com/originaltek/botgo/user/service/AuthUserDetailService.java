package com.originaltek.botgo.user.service;

import com.originaltek.botgo.user.dao.UserDao;
import com.originaltek.botgo.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * create_time : 2018/8/22 17:32
 * author      : chen.zhangchao
 * todo        :
 */
public class AuthUserDetailService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String userName) throws BadCredentialsException, UsernameNotFoundException {
        UserEntity entity = userDao.findByUserName(userName);
        if(entity == null){
            StringBuffer sb = new StringBuffer();
            sb.append("用户:");
            sb.append(userName);
            sb.append("  不存在");
            throw new BadCredentialsException(sb.toString());
        }
        return entity;
    }
}
