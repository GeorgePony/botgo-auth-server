package com.originaltek.botgo.user.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * create_time : 2018/8/22 17:27
 * author      : chen.zhangchao
 * todo        :
 */
@Data
public class UserEntity implements UserDetails, Serializable  {

    /**
     * 主键
     */
    private Integer userId;


    /**
     * 用户从属关系 (用户从属关系（关联该用户的创建者用户ID, -1为原始超级管理员用户)
     */
    private Integer dependence;

    /**
     * 用户名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录名
     */
    private String userName;

    /**
     * 单位住址
     */
    private String userAddr;

    /**
     * 用户创建时间
     */
    private Date createTime;

    /**
     * 标志位
     */
    private String userFlag;

    /**
     * 用户License
     */
    private String userLicense;

    /**
     * 使用期限开始时间
     */
    private Date beginTime;

    /**
     * 使用期限结束时间
     */
    private Date endTime;

    /**
     * 上锁
     * 1  解锁
     * 2  封禁
     */
    private Integer locked;



    private Date lastPasswordResetDate;



    private boolean accountNonExpired;

    private boolean credentialsNonExpired;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.locked == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
