package com.kozubek.gatewayapistarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApiStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApiStarterApplication.class, args);
    }

}
