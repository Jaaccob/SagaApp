package com.kozubek.userapplication.services;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userapplication.port.KeycloakUserPort;
import com.kozubek.userdomain.UserDomainService;
import com.kozubek.userdomain.core.User;
import com.kozubek.userdomain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final KeycloakUserPort keycloakUserPort;
    private final UserRepository userRepository;

    public UserId registerUser(RegisterUser commandUser) {
        User user = userDomainService.createUser(commandUser.userName(), commandUser.password(), commandUser.email());
//        userRepository.save(user);
        keycloakUserPort.registerUserInKeycloak(commandUser);
        return user.getId();
    }

    public AuthenticationJWTToken loginUser(AuthenticationUser userCommand) {
        return keycloakUserPort.loginUser(userCommand);
    }
}
