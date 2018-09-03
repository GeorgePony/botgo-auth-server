package com.originaltek.botgo.auth.cookie;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * create_time : 2018/8/31 16:17
 * author      : chen.zhangchao
 * todo        :
 */
public class JwtCookieUtil {

    private static final   String COOKIE_JWT = "jti";
    private static final   String COOKIE_ACCESS = "access_token";
    private static final   String REFRESH_ACCESS = "refresh_token";


    public static void addCookie(HttpServletResponse httpServletResponse , OAuth2AccessToken token){
        generateCookie(httpServletResponse, token , 3000);
    }

    public static void removeCookie(HttpServletResponse httpServletResponse){
        generateCookie(httpServletResponse , null , 0);
    }

    private static void generateCookie(HttpServletResponse httpServletResponse, OAuth2AccessToken token , int maxAge) {
        String jwt = (token==null || token.getAdditionalInformation().get("jti") == null)?"":
                token.getAdditionalInformation().get("jti").toString();
        Cookie jwtCookie = new Cookie(COOKIE_JWT , jwt);
        jwtCookie.setMaxAge(maxAge);
        jwtCookie.setPath("/");
        httpServletResponse.addCookie(jwtCookie);

        String access_token = token==null?"":token.getValue();
        Cookie accessCookie = new Cookie(COOKIE_ACCESS , access_token);
        accessCookie.setMaxAge(maxAge);
        accessCookie.setPath("/");
        httpServletResponse.addCookie(accessCookie);

        String refresh_token = token==null?"":token.getRefreshToken().getValue();
        Cookie refreshCookie = new Cookie(REFRESH_ACCESS , refresh_token);
        refreshCookie.setMaxAge(maxAge);
        refreshCookie.setPath("/");
        httpServletResponse.addCookie(refreshCookie);
    }


}
