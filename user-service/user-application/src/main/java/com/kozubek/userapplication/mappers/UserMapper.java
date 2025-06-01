package com.kozubek.userapplication.mappers;

import com.kozubek.commonapplication.dtos.AuthenticationUser;
import com.kozubek.commonapplication.dtos.RegisterUser;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userdomain.core.Role;
import com.kozubek.userdomain.core.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserMapper {

    public User registerUserToUserWithoutId(RegisterUser registerUser, Role role) {
        return User.builder()
                .username(registerUser.userName())
                .password(registerUser.password())
                .email(registerUser.email())
                .roles(Set.of(role))
                .build();
    }

    public User registerUserToUser(RegisterUser registerUser, String id, Role role) {
        return User.builder()
                .id(new UserId(UUID.fromString(id)))
                .username(registerUser.userName())
                .password(registerUser.password())
                .email(registerUser.email())
                .roles(Set.of(role))
                .build();
    }

    public User authenticationUserToUser(AuthenticationUser userCommand) {
        return User.builder()
                .username(userCommand.username())
                .password(userCommand.password())
                .build();
    }
}
