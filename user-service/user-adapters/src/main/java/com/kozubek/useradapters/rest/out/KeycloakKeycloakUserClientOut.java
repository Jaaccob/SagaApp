package com.kozubek.useradapters.rest.out;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.useradapters.rest.out.keycloak.KeycloakTokenProvider;
import com.kozubek.useradapters.rest.out.keycloak.RegisterUserInKeycloak;
import com.kozubek.useradapters.rest.out.keycloak.SetUserRoleInKeycloak;
import com.kozubek.userapplication.port.KeycloakUserPort;
import org.springframework.stereotype.Component;

@Component
public class KeycloakKeycloakUserClientOut implements KeycloakUserPort {

    private final RegisterUserInKeycloak registerUserInKeycloak;
    private final SetUserRoleInKeycloak setUserRoleInKeycloak;
    private final KeycloakTokenProvider keycloakTokenProvider;

    public KeycloakKeycloakUserClientOut(RegisterUserInKeycloak registerUserInKeycloak, SetUserRoleInKeycloak setUserRoleInKeycloak, KeycloakTokenProvider keycloakTokenProvider) {
        this.registerUserInKeycloak = registerUserInKeycloak;
        this.setUserRoleInKeycloak = setUserRoleInKeycloak;
        this.keycloakTokenProvider = keycloakTokenProvider;
    }

    public String registerUserInKeycloak(RegisterUser commandUser) {
        String accessToken = keycloakTokenProvider.getAdminAccessToken();
        String userId = registerUserInKeycloak.registerUser(commandUser, accessToken);
        setUserRoleInKeycloak.setDefaultRole(userId, accessToken);
        return userId;
    }

    public AuthenticationJWTToken loginUser(AuthenticationUser userCommand) {
        return keycloakTokenProvider.getAccessToken(userCommand);
    }
}
