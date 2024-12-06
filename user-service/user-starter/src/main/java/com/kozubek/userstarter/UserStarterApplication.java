package com.kozubek.userstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.kozubek")
public class UserStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStarterApplication.class, args);
    }
}

