package com.originaltek.botgo.user.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.originaltek.botgo.auth.ApplicationContextHolder;
import com.originaltek.botgo.auth.cookie.JwtCookieUtil;
import com.originaltek.botgo.auth.redis.JwtRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * create_time : 2018/8/30 12:00
 * author      : chen.zhangchao
 * todo        :
 */
@FrameworkEndpoint
public class JwtTokenEndPoints {


    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @RequestMapping("/oauth/revoke")
    @ResponseBody
    public String revokeToken(HttpServletRequest request , HttpServletResponse response , @RequestParam String access_token){
        if (consumerTokenServices.revokeToken(access_token)){
            SecurityContextHolder.getContext().setAuthentication(null);
            JwtCookieUtil.removeCookie(response);
            return "注销成功";
        }else{
            return "注销失败";
        }
    }



    @RequestMapping("/oauth/checkToken")
    @ResponseBody
    public JSONObject checkToken(HttpServletRequest request , HttpServletResponse response , @RequestParam String access_token){

       Map<String,Object> map = new HashMap<String,Object>();
       boolean flag = true;
       String msg = "";
        try {
            JwtRedisTokenStore redisTokenStore =  ApplicationContextHolder.getBean(JwtRedisTokenStore.class);
            OAuth2Authentication authentication =  redisTokenStore.readAuthentication(access_token);
            if(authentication == null){
                throw new Exception("用户验证失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            msg = e.getMessage();
        }
        map.put("flag" , flag);
        map.put("msg" , msg);
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject;
    }

    @RequestMapping("/oauth/login")
    @ResponseBody
    public void login(HttpServletRequest request , HttpServletResponse response , @RequestParam String username ,
                      @RequestParam String password){

    }




}
