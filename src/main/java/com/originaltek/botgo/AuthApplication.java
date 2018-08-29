package com.originaltek.botgo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * create_time : 2018/8/24 15:10
 * author      : chen.zhangchao
 * todo        :
 */
@SpringBootApplication
@EnableConfigurationProperties
@MapperScan("com.originaltek.botgo.user.dao")
@SessionAttributes("authorizationRequest")
public class AuthApplication {

    public static void main(String args[]){
        SpringApplication.run(AuthApplication.class , args);
    }

}
