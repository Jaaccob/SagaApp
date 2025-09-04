package com.kozubek.useradapters.rest.out.keycloak;

import com.kozubek.commonapplication.enums.SystemRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class SetUserRoleInKeycloak {
    private final WebClient webClient;
    private final String realmName;
    private static final String CLIENT_ID = "microservice-saga-app";

    public SetUserRoleInKeycloak(
            @Value("${keycloak.base-url}") final String baseUrl,
            @Value("${keycloak.realm-name}") final String realmName) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.realmName = realmName;
    }

    public Mono<Void> setDefaultRole(String userId, String accessToken) {
        String defaultRole = SystemRole.USER_ROLE.getRoleName();

        return getClientId(accessToken)
                .doOnNext(clientId -> log.debug("Client ID: {}", clientId))
                .flatMap(clientId -> getClientRoles(clientId, accessToken)
                        .doOnNext(clientRoles -> log.debug("Available client roles: {}", clientRoles))
                        .flatMap(clientRoles -> {
                            // Znajdź rolę USER_ROLE
                            Map<String, Object> userRole = clientRoles.stream()
                                    .filter(role -> defaultRole.equals(role.get("name")))
                                    .findFirst()
                                    .orElseThrow(() -> new RuntimeException("Role " + defaultRole + " not found"));

                            // Przypisz rolę do użytkownika
                            return setClientRoleToUser(accessToken, userId, clientId, List.of(userRole));
                        }));
    }

    private Mono<String> getClientId(String accessToken) {
        return webClient.get()
                .uri("/admin/realms/{realmName}/clients?clientId={clientId}", realmName, CLIENT_ID)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(List.class)
                .map(clients -> {
                    if (clients != null && !clients.isEmpty()) {
                        Map<String, Object> client = (Map<String, Object>) clients.get(0);
                        return client.get("id").toString();
                    }
                    throw new RuntimeException("Client not found: " + CLIENT_ID);
                });
    }

    private Mono<List<Map<String, Object>>> getClientRoles(String clientId, String accessToken) {
        return webClient.get()
                .uri("/admin/realms/{realmName}/clients/{clientId}/roles", realmName, clientId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
				});
    }

    private Mono<Void> setClientRoleToUser(String accessToken, String userId, String clientId, List<Map<String, Object>> roles) {
        return webClient.post()
                .uri("/admin/realms/{realmName}/users/{userId}/role-mappings/clients/{clientId}", realmName, userId, clientId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(roles)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
