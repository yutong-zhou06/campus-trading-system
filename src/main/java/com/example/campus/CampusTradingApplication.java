package com.example.campus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 校园二手交易系统 - 主应用类
 * 
 * @SpringBootApplication 注解的作用：
 * 1. @Configuration：标识这是一个配置类
 * 2. @EnableAutoConfiguration：自动配置Spring Boot应用
 * 3. @ComponentScan：扫描当前包及其子包，自动发现和注册组件
 */
@SpringBootApplication
public class CampusTradingApplication {

    /**
     * main方法：Java程序的入口点
     * 执行这个方法后，Spring Boot会：
     * 1. 启动内嵌的Tomcat服务器
     * 2. 初始化所有Spring组件
     * 3. 在指定端口（默认8080）监听HTTP请求
     */
    public static void main(String[] args) {
        SpringApplication.run(CampusTradingApplication.class, args);
        System.out.println("========================================");
        System.out.println("    校园二手交易系统启动成功！");
        System.out.println("    访问地址：http://localhost:8080");
        System.out.println("========================================");
    }
}