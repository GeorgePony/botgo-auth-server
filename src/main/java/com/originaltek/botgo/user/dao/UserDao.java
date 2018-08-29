package com.originaltek.botgo.user.dao;

import com.originaltek.botgo.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * create_time : 2018/8/22 17:27
 * author      : chen.zhangchao
 * todo        :
 */
@Mapper
@Repository
public interface UserDao {

    public UserEntity findByUserName(String userName);
}
