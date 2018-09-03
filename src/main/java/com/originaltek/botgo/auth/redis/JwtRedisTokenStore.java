package com.originaltek.botgo.auth.redis;

import com.originaltek.botgo.auth.cookie.JwtCookieUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * create_time : 2018/8/31 17:36
 * author      : chen.zhangchao
 * todo        : 自制的Redis 存储机制，除了存储Redis之外，还有对Response Cookie 的修改
 */
public class JwtRedisTokenStore extends RedisTokenStore {

    private static HttpServletResponse response ;


    public static void setResponse(HttpServletResponse httpServletResponse){
        response = httpServletResponse;
    }


    public JwtRedisTokenStore(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        super.storeAccessToken(token, authentication);
        if(response != null){
            JwtCookieUtil.addCookie(response , token);
        }

    }





}
