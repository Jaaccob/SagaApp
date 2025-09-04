package com.kozubek.useradapters.rest.out;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.useradapters.rest.out.keycloak.KeycloakTokenProvider;
import com.kozubek.useradapters.rest.out.keycloak.RegisterUserInKeycloak;
import com.kozubek.useradapters.rest.out.keycloak.SetUserRoleInKeycloak;
import com.kozubek.userapplication.port.KeycloakUserPort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class KeycloakKeycloakUserClientOut implements KeycloakUserPort {

    private final RegisterUserInKeycloak registerUserInKeycloak;
    private final SetUserRoleInKeycloak setUserRoleInKeycloak;
    private final KeycloakTokenProvider keycloakTokenProvider;

    public KeycloakKeycloakUserClientOut(final RegisterUserInKeycloak registerUserInKeycloak, final SetUserRoleInKeycloak setUserRoleInKeycloak, final KeycloakTokenProvider keycloakTokenProvider) {
        this.registerUserInKeycloak = registerUserInKeycloak;
        this.setUserRoleInKeycloak = setUserRoleInKeycloak;
        this.keycloakTokenProvider = keycloakTokenProvider;
    }

    public Mono<String> registerUserInKeycloak(final RegisterUser commandUser) {
        return keycloakTokenProvider.getAdminAccessToken()
                .flatMap(accessToken ->
                        registerUserInKeycloak.registerUser(commandUser, accessToken)
                                .flatMap(userId ->
                                        setUserRoleInKeycloak.setDefaultRole(userId, accessToken)
                                                .thenReturn(userId)
                                )
                );
    }

    public Mono<AuthenticationJWTToken> loginUser(final AuthenticationUser userCommand) {
        return keycloakTokenProvider.getAccessToken(userCommand);
    }
}
