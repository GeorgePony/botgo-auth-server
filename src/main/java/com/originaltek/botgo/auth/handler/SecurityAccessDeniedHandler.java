package com.originaltek.botgo.auth.handler;

import com.alibaba.fastjson.JSON;
import com.originaltek.botgo.auth.entity.AuthEntity;
import com.originaltek.botgo.user.entity.UserEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create_time : 2018/8/27 11:47
 * author      : chen.zhangchao
 * todo        :
 */
@Component
public class SecurityAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        AuthEntity authEntity = new AuthEntity("Login Denied" , "001" , "");
        httpServletResponse.getWriter().write(JSON.toJSONString(authEntity));
    }
}
