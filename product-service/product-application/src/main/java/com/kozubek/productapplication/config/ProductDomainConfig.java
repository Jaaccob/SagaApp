package com.kozubek.productapplication.config;

import com.kozubek.productdomain.ProductDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductDomainConfig {
    @Bean
    public ProductDomainService productDomainService() {
        return new ProductDomainService();
    }
}

