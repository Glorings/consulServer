package com.example.consulserver.controller;



import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
}
