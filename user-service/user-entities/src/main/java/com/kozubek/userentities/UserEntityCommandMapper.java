package com.kozubek.userentities;

import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userdomain.core.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserEntityCommandMapper {

    private final RoleEntityCommandMapper roleEntityCommandMapper;

    public User userEntityToUser(UserEntity userEntity) {
        return User.builder()
                .id(new UserId(userEntity.getId()))
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .roles(roleEntityCommandMapper.roleEntitiesToRoles(userEntity.getRoleEntities()))
                .build();
    }

    public UserEntity userToUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().id())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roleEntities(roleEntityCommandMapper.rolesToRoleEntities(user.getRoles()))
                .build();
    }
}
