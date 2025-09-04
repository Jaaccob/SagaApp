package com.kozubek.useradapters.rest.out.keycloak;

import com.kozubek.commonapplication.dtos.RegisterUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class RegisterUserInKeycloak {

    private final WebClient webClient;
    private final String realmName;

    public RegisterUserInKeycloak(
            @Value("${keycloak.base-url}") final String baseUrl,
            @Value("${keycloak.realm-name}") final String realmName) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.realmName = realmName;
    }

    public Mono<String> registerUser(RegisterUser commandUser, String accessToken) {
        return userExistsInKeycloak(commandUser, accessToken)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("User already exists"));
                    }

                    Map<String, Object> body = buildUserPayload(commandUser);
                    return webClient.post()
                            .uri("/admin/realms/{realmName}/users", realmName)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                            .bodyValue(body)
                            .retrieve()
                            .toBodilessEntity()
                            .onErrorMap(e -> new RuntimeException("Failed to register user in Keycloak", e))
                            .map(this::getUserIdFromHeader);
                });
    }

    private Mono<Boolean> userExistsInKeycloak(RegisterUser commandUser, String accessToken) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/realms/{realmName}/users")
                        .queryParam("username", commandUser.userName())
                        .queryParam("exact", true) //?
                        .build(realmName))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(List.class)
                .map(users -> !users.isEmpty())
                .onErrorReturn(false);
    }

    private String getUserIdFromHeader(ResponseEntity<Void> response) {
        String locationHeader = response.getHeaders().getFirst(HttpHeaders.LOCATION);
        if (locationHeader == null) {
            throw new RuntimeException("Location header not found in response");
        }
        return locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
    }

    private Map<String, Object> buildUserPayload(final RegisterUser commandUser) {
        Map<String, Object> credentials = new HashMap<>();
        credentials.put("type", "password");
        credentials.put("value", commandUser.password());
        credentials.put("temporary", false);

        Map<String, Object> payload = new HashMap<>();
        payload.put("username", commandUser.userName());
        payload.put("email", commandUser.email());
        payload.put("firstName", "qba");
        payload.put("lastName", "qba");
        payload.put("enabled", true);
        payload.put("emailVerified", true);
        payload.put("credentials", List.of(credentials));

        return payload;
    }
}
