package com.originaltek.botgo.auth.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * create_time : 2018/8/24 15:11
 * author      : chen.zhangchao
 * todo        :
 */
@Configuration
@EnableAuthorizationServer
public class BotGoAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;


    @Resource
    private DataSource dataSource;



    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 配置在数据库oauth_client_details 表中，  之后可以拓展
         */
        clients.withClientDetails(getClientDetailsService());


        /**
         * 配置在内存中

        clients.inMemory()
                .withClient("client_1")
                .secret("123456")
                .redirectUris("http://baidu.com")
                .authorizedGrantTypes("authorization_code"
                        , "client_credentials", "refresh_token",
                        "password", "implicit"
                )
                .authorities("ROLE_CLIENT")
                .scopes("openid" , "select")
        .and()
                .withClient("client_5")
                .secret("123456")
                .redirectUris("http://baidu.com")
                .authorizedGrantTypes("authorization_code"
                        , "client_credentials", "refresh_token",
                        "password", "implicit"
                )
                .authorities("ROLE_CLIENT")
                .scopes("openid" , "select")
        ;
         */
    }


    /**
     * @return
     */
    @Bean
    public ClientDetailsService getClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }


    /**
     * token Redis 存储的Bean
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redis = new RedisTokenStore(redisConnectionFactory);
        return redis;
    }

    /**
     * 配置AuthorizationServerEndpointsConfigurer  众多相关类 ， 配置身份认证器、认证方式
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())
                //.tokenStore(new InMemoryTokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                // 2018-4-3 增加配置，允许 GET、POST 请求获取 token，即访问端点：oauth/token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

        endpoints.reuseRefreshTokens(true);

        if(jwtAccessTokenConverter != null && jwtTokenEnhancer != null){
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancerList = new ArrayList();
            enhancerList.add(jwtTokenEnhancer);
            enhancerList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(enhancerList);

            endpoints.tokenEnhancer(tokenEnhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();
    }
}
