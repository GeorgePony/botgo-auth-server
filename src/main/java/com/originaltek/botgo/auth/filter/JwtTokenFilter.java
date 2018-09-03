package com.originaltek.botgo.auth.filter;

import com.originaltek.botgo.auth.ApplicationContextHolder;
import com.originaltek.botgo.auth.cookie.JwtCookieUtil;
import com.originaltek.botgo.auth.redis.JwtRedisTokenStore;
import com.originaltek.botgo.user.entity.UserEntity;
import com.originaltek.botgo.user.service.AuthUserDetailService;
import com.originaltek.botgo.user.util.JwtTokenUtil;
import com.originaltek.botgo.user.util.StringUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * create_time : 2018/8/27 14:11
 * author      : chen.zhangchao
 * todo        :
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {


    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private SecurityHttpSessionListener myHttpSessionListener;


    @Autowired
    AuthorizationServerTokenServices tokenServices;
    @Autowired
    ClientDetailsService clientDetailsService;
//    @Autowired
//    OAuth2RequestFactory requestFactory;


    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)  {
        String authHeader = httpServletRequest.getHeader("Authorization");
        String access_token = httpServletRequest.getHeader("access_token");
        String refresh_token = httpServletRequest.getHeader("refresh_token");
        JwtRedisTokenStore.setResponse(httpServletResponse);
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                final String authToken = authHeader.substring("Bearer ".length());
                Map<String,Object> parseResult = JwtTokenUtil.parseToken(authToken);
                int resultType = MapUtils.getInteger(parseResult , "result");

                if(!StringUtil.hasText(access_token)){
                    throw new Exception("请求Header中无access_token 信息");
                }
                RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory) ;
                OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(access_token);
                if(oAuth2Authentication == null){
                    SecurityContextHolder.getContext().setAuthentication(null);
                    throw new Exception("用户已注销");
                }


                /**
                 * 没有错误的情况下
                 */
                if(resultType == JwtTokenUtil.TOKEN_CORRECT){
                    String username = MapUtils.getString(parseResult , "subject");
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = ApplicationContextHolder.getBean(AuthUserDetailService.class).loadUserByUsername(username);
                        if (userDetails != null) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }

                /**
                 * 密码过期  需要刷新
                 */
                else if (resultType == JwtTokenUtil.TOKEN_ERR_EXPIRED){
                    String clientId = "client_7";
                    String clientSecret = "123456";
                    String grantType = "refresh_token";
                    Map<String,String> tokenMap = new HashMap<String, String>(4);
                    tokenMap.put("client_id" , clientId);
                    tokenMap.put("grant_type" , grantType);
                    tokenMap.put("client_secret" , clientSecret);
                    tokenMap.put("refresh_token" , refresh_token);
                    TokenRequest tokenRequest = new TokenRequest(tokenMap , clientId , new ArrayList<String>() , grantType);
    //                RefreshTokenGranter granter = new RefreshTokenGranter();
                    OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
                    RefreshTokenGranter granter = new RefreshTokenGranter(tokenServices , clientDetailsService , requestFactory);
                    OAuth2AccessToken oAuth2AccessToken = granter.grant(grantType , tokenRequest);
                    oAuth2AccessToken.getValue();
                    JwtCookieUtil.addCookie(httpServletResponse , oAuth2AccessToken);
                }


            }else{
                String uri = httpServletRequest.getRequestURI();
                if(!uri.startsWith("/oauth")){
                    SecurityContextHolder.getContext().setAuthentication(null);
                }
            }


        } catch (BadCredentialsException e) {


        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }


        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            //e.printStackTrace();
        }


        /**
        String authHeader = httpServletRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String authToken = authHeader.substring("Bearer ".length());
                String username = JwtTokenUtil.parseToken(authToken);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
                {
                    AuthUserDetailService userDetailsService =  ApplicationContextHolder.getBean(AuthUserDetailService.class);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
                filterChain.doFilter(httpServletRequest, httpServletResponse);
         */
    }


    private boolean isCheck(String username){
        boolean isCheck = true;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!StringUtil.hasText(username)){
            return false;
        }
        UserEntity userEntity =  (UserEntity)(authentication.getPrincipal());
        if(!username.equals(userEntity.getUsername())){
            return false;
        }
        return true;


    }

}
