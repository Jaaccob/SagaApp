package com.kozubek.userapplication.port;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import reactor.core.publisher.Mono;

public interface KeycloakUserPort {

    Mono<String> registerUserInKeycloak(RegisterUser commandUser);

    Mono<AuthenticationJWTToken> loginUser(AuthenticationUser userCommand);
}
