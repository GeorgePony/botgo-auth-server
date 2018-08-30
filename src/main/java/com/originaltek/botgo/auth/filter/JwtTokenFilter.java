package com.originaltek.botgo.auth.filter;

import com.originaltek.botgo.auth.ApplicationContextHolder;
import com.originaltek.botgo.user.entity.UserEntity;
import com.originaltek.botgo.user.service.AuthUserDetailService;
import com.originaltek.botgo.user.util.JwtTokenUtil;
import com.originaltek.botgo.user.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {



            final String authToken = authHeader.substring("Bearer ".length());

            String username = JwtTokenUtil.parseToken(authToken);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = ApplicationContextHolder.getBean(AuthUserDetailService.class).loadUserByUsername(username);
                if (userDetails != null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }else{
            String uri = httpServletRequest.getRequestURI();
            if(!uri.startsWith("/oauth")){
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

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
