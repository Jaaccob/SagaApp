package com.kozubek.gatewayapistarter.configurations;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Bean
    public RouteLocator gatewayRoutes(final RouteLocatorBuilder builder) {
        return builder.routes()
                .route("discovery-service", route -> route.path("/eureka/web")
                        .filters(filter -> filter.setPath("/"))
                        .uri("http://localhost:8761"))
                .route("discovery-service-static", route -> route.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .route("user-service", route -> route.path("/api/user/**")
                        .uri("lb://user-service"))
                .route("order-service", route -> route.path("/api/order/**")
                        .uri("lb://order-service"))
                .route("inventory-service", route -> route.path("/api/inventory/**")
                        .uri("lb://inventory-service"))
                .route("payment-service", route -> route.path("/api/payment/**")
                        .uri("lb://payment-service"))
                .route("product-service", route -> route.path("/api/product/**")
                        .uri("lb://product-service"))
                .build();
    }
}
