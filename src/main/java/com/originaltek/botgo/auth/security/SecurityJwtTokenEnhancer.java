package com.originaltek.botgo.auth.security;

import com.originaltek.botgo.user.entity.UserEntity;
import com.originaltek.botgo.user.util.JwtTokenUtil;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * create_time : 2018/8/27 15:40
 * author      : chen.zhangchao
 * todo        :
 */
public class SecurityJwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        Date expireDate = accessToken.getExpiration();
        Map<String,Object> additionalInfo = accessToken.getAdditionalInformation();
        Set<String> scopes = accessToken.getScope();

        Object principal = authentication.getPrincipal();
        String userName = "";
        if(principal instanceof String){
            userName = principal.toString();
        }else{
            UserEntity entity = (UserEntity)authentication.getPrincipal();
            userName = entity.getUsername();
        }

        String jwtToken = JwtTokenUtil.generateToken( userName , 1000 );
        DefaultOAuth2AccessToken newAccessToken = new DefaultOAuth2AccessToken(jwtToken);
        newAccessToken.setAdditionalInformation(additionalInfo);
        newAccessToken.setExpiration(expireDate);
        newAccessToken.setRefreshToken(refreshToken);
        newAccessToken.setScope(scopes);


        return newAccessToken;
//        return accessToken;
//        Map<String, Object> info = new HashMap<String , Object>();
//        info.put("blog", "https://longfeizheng.github.io/");//扩展返回的token
//        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
//        return accessToken;
    }
}
