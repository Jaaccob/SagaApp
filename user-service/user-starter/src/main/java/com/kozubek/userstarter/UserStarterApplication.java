package com.kozubek.userstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.kozubek.useradapters.repository"})
@EntityScan(basePackages = {"com.kozubek.userentities"})
@SpringBootApplication(scanBasePackages = "com.kozubek")
public class UserStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserStarterApplication.class, args);
    }
}

