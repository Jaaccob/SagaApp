package com.kozubek.useradapters.rest.out.keycloak;

import com.kozubek.commonapplication.dtos.RegisterUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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

    public String registerUser(RegisterUser commandUser, String accessToken) {
        if (userExistsInKeycloak(commandUser, accessToken)) {
            throw new RuntimeException("User already exists");
        }

        Map<String, Object> body = buildUserPayload(commandUser);
        ResponseEntity<Void> response = webClient.post()
                .uri("/admin/realms/{realmName}/users", realmName)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .onErrorMap(e -> new RuntimeException("Failed to register user in Keycloak", e)) //todo - change this error to something precise
                .block();

        if (Objects.isNull(response)) {
            throw new RuntimeException("Failed to register user in Keycloak"); //todo - change this error to something precise
        }

        return getUserIdFromHeader(response);
    }

    private boolean userExistsInKeycloak(RegisterUser commandUser, String accessToken) {
        List<?> result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/admin/realms/{realmName}/users")
                        .queryParam("username", commandUser.userName())
                        .build(realmName))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(List.class)
                .block();
        return Objects.nonNull(result) && !result.isEmpty();
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

    private String getUserIdFromHeader(ResponseEntity<Void> response) {
        String location = response.getHeaders()
                .getLocation()
                .toString();
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
