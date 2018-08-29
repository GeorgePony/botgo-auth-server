package com.originaltek.botgo.auth.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create_time : 2018/8/22 16:01
 * author      : chen.zhangchao
 * todo        :
 */
@RestController
public class TestEndpoints {

    @RequestMapping("/product/{id}")

    public String getProduct(@PathVariable String id) {
        //for debug
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "product id : " + id;
    }

    @RequestMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        //for debug
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "order id : " + id;
    }

//    @RequestMapping("/login")
//    public ModelAndView getLogin(HttpServletRequest request , HttpServletResponse httpServletResponse , HttpSession session){
//        return new ModelAndView("login");
//    }

}