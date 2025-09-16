package com.kozubek.productstarter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = {"com.kozubek.productadapters.repository"})
@EntityScan(basePackages = {"com.kozubek.productentities"})
@SpringBootApplication(scanBasePackages = "com.kozubek")
public class ProductStarterApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ProductStarterApplication.class, args);
	}

}
