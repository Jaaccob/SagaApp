package com.kozubek.useradapters.rest.out.keycloak;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class SetUserRoleInKeycloak {
    private final WebClient webClient;
    private final String realmName;

    public SetUserRoleInKeycloak(
            @Value("${keycloak.base-url}") final String baseUrl,
            @Value("${keycloak.realm-name}") final String realmName) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.realmName = realmName;
    }

    public void setDefaultRole(String userId, String accessToken) {
        String defaultRole = "USER_ROLE";
        Map<String, Object> roles = fetchRolesFromKeycloak(accessToken, defaultRole);
        setRoleToUser(accessToken, userId, roles);
    }

    private Map<String, Object> fetchRolesFromKeycloak(String accessToken, String defaultRole) {
        return webClient.get()
                .uri("/admin/realms/{realmName}/roles/{roleName}", realmName, defaultRole)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    private void setRoleToUser(String accessToken, String userId, Map<String, Object> roles ) {
        webClient.post()
                .uri("/admin/realms/{realmName}/users/{userId}/role-mappings/realm", realmName, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(List.of(roles))
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
