package com.originaltek.botgo.auth.handler;

import com.alibaba.fastjson.JSON;
import com.originaltek.botgo.auth.entity.AuthEntity;
import com.originaltek.botgo.user.entity.UserEntity;
import com.originaltek.botgo.user.util.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * create_time : 2018/8/27 10:23
 * author      : chen.zhangchao
 * todo        :
 */
@Component
public class SecurityAuthenticationSuccessHandler
        //implements AuthenticationSuccessHandler
    extends SavedRequestAwareAuthenticationSuccessHandler
{
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
       /**
        UserEntity entity = (UserEntity)authentication.getPrincipal();
        String jwtToken = JwtTokenUtil.generateToken( entity.getUsername() , 1000 );
        AuthEntity authEntity = new AuthEntity("Login Success" , "000" , jwtToken);

        Cookie cookie = new Cookie("token" , jwtToken);
        cookie.setMaxAge(1000);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);


        RequestCache requestCache = new HttpSessionRequestCache() ;
        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest , httpServletResponse);
        String url = null;
        if(savedRequest != null){
            url = savedRequest.getRedirectUrl();
        }
        if(url == null){
            url = "index";
        }
        getRedirectStrategy().sendRedirect(httpServletRequest , httpServletResponse , url);
        */

       //System.out.print("123");
        RequestCache requestCache = new HttpSessionRequestCache() ;
        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest , httpServletResponse);
        String userName = httpServletRequest.getParameter("username");
        String password = httpServletRequest.getParameter("password");
        String clientId = "client_7";
        String client_secret = "123456";
        String grant_type = "password";
        String scope = "select";





        String url = null;
        if(savedRequest != null){
            url = ((DefaultSavedRequest) savedRequest).getRequestURI();
        }
        if(url == null){
            url = "index";
        }
        getRedirectStrategy().sendRedirect(httpServletRequest , httpServletResponse , url);

    }






}
