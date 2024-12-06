package com.kozubek.orderstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.kozubek")
public class OrderStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderStarterApplication.class, args);
    }
}
