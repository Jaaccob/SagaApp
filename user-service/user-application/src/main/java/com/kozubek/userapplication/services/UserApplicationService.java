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
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserApplicationService {

    private final UserDomainService userDomainService;
    private final RoleApplicationService roleApplicationService;
    private final KeycloakUserPort keycloakUserPort;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Mono<UserId> registerUser(RegisterUser commandUser) {
        Role role = roleApplicationService.getRoleFromCache(SystemRole.USER_ROLE);
        User user = userMapper.registerUserToUserWithoutId(commandUser, role);
        UserCreatedEvent userCreatedEvent = userDomainService.createUser(user);

        return keycloakUserPort.registerUserInKeycloak(commandUser)
                .map(userId -> {
                    User userWithId = userMapper.registerUserToUser(commandUser, userId, role);
                    userRepository.save(userWithId);
                    log.info("User created: {}", userCreatedEvent.getUser());
                    return userWithId.getId();
                });
    }

    public Mono<AuthenticationJWTToken> loginUser(AuthenticationUser userCommand) {
        User user = userMapper.authenticationUserToUser(userCommand);
        UserLoggedEvent userLoggedEvent = userDomainService.logUser(user);
        log.info("User logged: {}", userLoggedEvent.getUser());
        return keycloakUserPort.loginUser(userCommand);
    }
}
