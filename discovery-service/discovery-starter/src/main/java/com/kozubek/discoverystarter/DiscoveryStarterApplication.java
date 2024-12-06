package com.kozubek.discoverystarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication(scanBasePackages = "com.kozubek")
@EnableEurekaServer
public class DiscoveryStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryStarterApplication.class, args);
    }

}
