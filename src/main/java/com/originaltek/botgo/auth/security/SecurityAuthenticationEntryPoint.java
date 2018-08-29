package com.originaltek.botgo.auth.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.originaltek.botgo.auth.entity.AuthEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create_time : 2018/8/27 14:19
 * author      : chen.zhangchao
 * todo        :
 */
@Component
public class SecurityAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        if(isAjaxRequest(httpServletRequest)){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
        }else{
            httpServletResponse.sendRedirect("/login");
        }

    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
    }


}
