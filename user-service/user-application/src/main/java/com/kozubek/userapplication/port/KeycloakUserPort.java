package com.kozubek.userapplication.port;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;

public interface KeycloakUserPort {

    String registerUserInKeycloak(RegisterUser commandUser);

    AuthenticationJWTToken loginUser(AuthenticationUser userCommand);
}
