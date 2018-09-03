package com.originaltek.botgo.auth.security;

import com.originaltek.botgo.auth.filter.JwtTokenFilter;
import com.originaltek.botgo.auth.handler.SecurityAccessDeniedHandler;
import com.originaltek.botgo.auth.handler.SecurityAuthenticationSuccessHandler;
import com.originaltek.botgo.user.service.AuthDaoAuthenticationProvider;
import com.originaltek.botgo.user.service.AuthUserDetailService;
import com.originaltek.botgo.user.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * create_time : 2018/8/24 15:45
 * author      : chen.zhangchao
 * todo        :
 */
@Configuration
@EnableWebSecurity
@Order(108)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private SecurityAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private SecurityAccessDeniedHandler securityAccessDeniedHandler;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;


    @Autowired
    private SecurityAuthenticationEntryPoint securityAuthenticationEntryPoint;


    @Value("${security.ignorePath}")
    private String ignorePaths;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(!StringUtil.hasText(ignorePaths)){
            ignorePaths = "";
        }
        String[] paths = ignorePaths.split(",");

        /**
         * permitAll  路径必须在anyRequest 前面
         */
        http
                .authorizeRequests()
                .antMatchers(paths).permitAll()
                .anyRequest()
                .authenticated()

                .and()
                .formLogin()
                .successHandler(authenticationSuccessHandler)
                .permitAll()
        .and().csrf().disable();

        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(userDetailsService()).tokenValiditySeconds(300);

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);


        //http.exceptionHandling().accessDeniedHandler(securityAccessDeniedHandler);


    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        DaoAuthenticationProvider authenticationProvider = new AuthDaoAuthenticationProvider();
        //DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        List<AuthenticationProvider> providers = new ArrayList<AuthenticationProvider>(4);
        providers.add(authenticationProvider);
        ProviderManager providerManager = new ProviderManager(providers);
        return providerManager;
    }

    @Bean("jwtUserDetailsService")
    @Override
    protected UserDetailsService userDetailsService(){
        return new AuthUserDetailService();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder( new SecurityAuthEncoder());
    }






}
