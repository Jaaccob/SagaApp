package com.kozubek.userentities;

import com.kozubek.userdomain.core.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserEntityCommandMapper {

    private final RoleEntityCommandMapper roleEntityCommandMapper;

    public User userEntityToUser(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getUsername(), userEntity.getPassword(), userEntity.getEmail(), roleEntityCommandMapper.roleEntitiesToRoles(userEntity.getRoleEntities()));
    }

    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleEntities(roleEntityCommandMapper.rolesToRoleEntities(user.getRoles()))
                .build();
    }
}
