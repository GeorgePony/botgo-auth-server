package com.originaltek.botgo.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * create_time : 2018/8/27 10:29
 * author      : chen.zhangchao
 * todo        :
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthEntity implements Serializable {
    private String msg;
    private String status;
    private String jwtToken;
}
