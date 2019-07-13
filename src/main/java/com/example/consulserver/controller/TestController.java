package com.example.consulserver.controller;



import com.alibaba.fastjson.JSON;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 注意：新版Spring Cloud Consul 默认注册健康检查接口为：/actuator/health
     * @return SUCCESS
     */
    @GetMapping("/actuator/health")
    public String health() {
        return "SUCCESS";
    }

    /**
     * 提供 sayHello 服务:根据对方传来的名字 XX，返回:hello XX
     * @return String
     */
    @GetMapping("/sayHello")
    public String sayHello(String name){
        return "hello," + name + ". I am tag2";
    }


    @GetMapping("/test/cookie")
    public String testGateway(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName()+":"+cookie.getValue());
            }
        }
        return "Spring Cloud Gateway,Hello world!";
    }

    /**
     * 测试Cookies路由断言工厂
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/test/cookie2")
    public String cookie2(HttpServletRequest request, HttpServletResponse response){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object user = servletRequestAttributes.getAttribute("user", RequestAttributes.SCOPE_SESSION);
        Object user2= servletRequestAttributes.getAttribute("user", RequestAttributes.SCOPE_REQUEST);
        System.out.println(JSON.toJSONString(user));
        System.out.println(JSON.toJSONString(user2));
        return "TTTT!";
    }

}
