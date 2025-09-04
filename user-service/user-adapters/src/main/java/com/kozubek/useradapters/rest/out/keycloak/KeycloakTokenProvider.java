package com.kozubek.useradapters.rest.out.keycloak;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class KeycloakTokenProvider {

    private final WebClient webClient;
    private final String realmName;
    private final String clientSecret;

    public KeycloakTokenProvider(
            @Value("${keycloak.base-url}") final String baseUrl,
            @Value("${keycloak.realm-name}") final String realmName,
            @Value("${keycloak.client-secret}") final String clientSecret) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.realmName = realmName;
        this.clientSecret = clientSecret;
    }

    public Mono<String> getAdminAccessToken() {
        BodyInserters.FormInserter<String> body = getMetaData("admin", "admin", "admin-cli");

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", "master")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }

    public Mono<AuthenticationJWTToken> getAccessToken(AuthenticationUser userCommand) {
        BodyInserters.FormInserter<String> body = getMetaData(userCommand.username(), userCommand.password(), "microservice-saga-app")
                .with("client_secret", clientSecret);

        return webClient.post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realmName)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .bodyToMono(AuthenticationJWTToken.class);
    }

    private BodyInserters.FormInserter<String> getMetaData(String username, String password, String clientId) {
        return BodyInserters.fromFormData("grant_type", "password")
                .with("client_id", clientId)
                .with("username", username)
                .with("password", password);
    }
}
