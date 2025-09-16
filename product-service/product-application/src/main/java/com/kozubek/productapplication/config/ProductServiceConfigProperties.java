package com.kozubek.productapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "product-service")
public class ProductServiceConfigProperties {
    private String productCreatedTopicName;
}
