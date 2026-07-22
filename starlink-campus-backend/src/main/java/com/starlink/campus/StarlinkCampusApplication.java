package com.starlink.campus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.starlink.campus.module.**.mapper")
public class StarlinkCampusApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarlinkCampusApplication.class, args);
        System.out.println("=================================================");
        System.out.println("🚀 海星智联智慧校园后端服务 (Spring Boot 3.2) 启动成功！");
        System.out.println("=================================================");
    }
}
