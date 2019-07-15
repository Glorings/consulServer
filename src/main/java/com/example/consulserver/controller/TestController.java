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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestController {

    protected final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap<>();

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

    /**
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/test/head")
    public String testGatewayHead(HttpServletRequest request, HttpServletResponse response){
        String head=request.getHeader("X-Request-Acme");
        return "return head info:"+head;
    }

    @GetMapping("/test/addRequestParameter")
    public String addRequestParameter(HttpServletRequest request, HttpServletResponse response){
        String parameter=request.getParameter("example");
        return "return addRequestParameter info:"+parameter;
    }

    @GetMapping("/retry")
    public String testRetryByException(@RequestParam("key") String key, @RequestParam(name = "count") int count) {
        AtomicInteger num = map.computeIfAbsent(key, s -> new AtomicInteger());
        //对请求或重试次数计数
        int i = num.incrementAndGet();
        logger.warn("重试次数: "+i);
        //计数i小于重试次数2抛出异常，让Spring Cloud Gateway进行重试
        if (i < count) {
            throw new RuntimeException("Deal with failure, please try again!");
        }
        //当重试两次时候，清空计数，返回重试两次成功
        map.clear();
        return "重试"+count+"次成功！";
    }

    @GetMapping("/customFilter")
    public String customFilter(@RequestParam String name) {
        return "customFilter, " + name + "!";
    }

}
