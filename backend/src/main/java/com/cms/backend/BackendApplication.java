package com.cms.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.cms")
@MapperScan("com.cms.backend.mapper")
public class BackendApplication {

    public static final String NGINX_BASE_PATH = "D:\\nginx\\nginx-1.26.2\\ICPRFiles\\";

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
