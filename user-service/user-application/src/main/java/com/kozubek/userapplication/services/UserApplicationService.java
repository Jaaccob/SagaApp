package com.kozubek.userapplication.services;

import com.kozubek.commonapplication.dtos.AuthenticationJWTToken;
import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.commonapplication.enums.SystemRole;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userapplication.mappers.UserMapper;
import com.kozubek.userapplication.port.KeycloakUserPort;
import com.kozubek.userdomain.UserDomainService;
import com.kozubek.userdomain.core.Role;
import com.kozubek.userdomain.core.User;
import com.kozubek.userdomain.events.UserCreatedEvent;
import com.kozubek.userdomain.events.UserLoggedEvent;
import com.kozubek.userdomain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final RoleApplicationService roleApplicationService;
    private final KeycloakUserPort keycloakUserPort;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserId registerUser(RegisterUser commandUser) {
        Role role = roleApplicationService.getRoleFromCache(SystemRole.USER_ROLE);
        User user = userMapper.registerUserToUserWithoutId(commandUser, role);
        UserCreatedEvent userCreatedEvent = userDomainService.createUser(user);

        String userId = keycloakUserPort.registerUserInKeycloak(commandUser);
        user = userMapper.registerUserToUser(commandUser, userId, role);
        userRepository.save(user);

        log.info("User created: {}", userCreatedEvent.getUser());
        return user.getId();
    }

    public AuthenticationJWTToken loginUser(AuthenticationUser userCommand) {
        User user = userMapper.authenticationUserToUser(userCommand);

        UserLoggedEvent userLoggedEvent = userDomainService.logUser(user);
        AuthenticationJWTToken token = keycloakUserPort.loginUser(userCommand);

        log.info("User logged: {}", userLoggedEvent.getUser());
        return token;
    }
}
