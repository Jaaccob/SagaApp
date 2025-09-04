package com.kozubek.gatewayapistarter.configurations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/user/auth/register", "/api/user/auth/login").permitAll()
                        .pathMatchers("/api/payment/**").hasAnyAuthority("ROLE_FINANCIAL_MANAGER", "ROLE_SENIOR_MANAGER")
                        .pathMatchers("/api/inventory/**").hasAnyAuthority("ROLE_INVENTORY_MANAGER", "ROLE_SENIOR_MANAGER")
                        .pathMatchers("/api/order/**").hasAnyAuthority("ROLE_ORDER_MANAGER", "ROLE_SENIOR_MANAGER")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            final List<String> authorities = new ArrayList<>();

            log.debug("Raw JWT token claims: {}", jwt.getClaims());

            final Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            log.debug("Realm access data: {}", realmAccess);
            if (realmAccess != null) {
                @SuppressWarnings("unchecked") final List<String> roles = (List<String>) realmAccess.get("roles");
                log.debug("Roles from realm_access: {}", roles);
                if (roles != null) {
                    authorities.addAll(roles);
                }
            }

            final Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
            log.debug("Resource access data: {}", resourceAccess);
            if (resourceAccess != null) {
                @SuppressWarnings("unchecked") final Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get("microservice-saga-app");
                if (clientAccess != null) {
                    @SuppressWarnings("unchecked") final List<String> resourceRoles = (List<String>) clientAccess.get("roles");
                    log.debug("Roles from resource_access: {}", resourceRoles);
                    if (resourceRoles != null) {
                        authorities.addAll(resourceRoles);
                    }
                }
            }

            @SuppressWarnings("unchecked") final List<String> groups = (List<String>) jwt.getClaims().get("groups");
            log.debug("Groups from token: {}", groups);
            if (groups != null) {
                authorities.addAll(groups);
            }

            log.debug("Final authorities list: {}", authorities);
            return authorities.stream()
                    .distinct()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        });

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}
