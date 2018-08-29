package com.originaltek.botgo.auth.security;

import com.originaltek.botgo.user.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * create_time : 2018/8/27 9:54
 * author      : chen.zhangchao
 * todo        :
 */
public class SecurityAuthEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence inputpassword) {
        String encodePass = MD5Util.MD5SaltEncode(inputpassword.toString() ).toString();
        return encodePass;
    }

    @Override
    public boolean matches(CharSequence inputPassword, String storedPassword) {
        String encodePass = MD5Util.MD5SaltEncode(inputPassword.toString() ).toString();
        if(encodePass.equals(storedPassword)){
            return true;
        }
        return false;
    }
}
