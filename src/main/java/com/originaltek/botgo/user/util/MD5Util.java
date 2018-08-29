package com.originaltek.botgo.user.util;

import com.originaltek.botgo.auth.ApplicationContextHolder;
import lombok.Data;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * create_time : 2018/8/17 10:59
 * author      : chen.zhangchao
 * todo        : MD5验证工具类
 */
@Component
@Data
public class MD5Util {

    /**
     * Md5 盐值
     */
    @Value("${password.salt}")
    private String passwordSalt;



    /**
     * MD5盐值加密  加密次数为1024次
     */
    public static Object MD5SaltEncode(String credentials ){
        String hashAlgorithmName="MD5";

        MD5Util util = ApplicationContextHolder.getBean(MD5Util.class);

        Object salt =ByteSource.Util.bytes(util.getPasswordSalt());  //使用一系列无规则字母数字符号作为 盐值加密
        int hashIterations =1024;
        Object result = new SimpleHash(hashAlgorithmName,credentials,salt,hashIterations);
        return result;
    }
}
