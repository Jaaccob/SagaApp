package com.kozubek.userentities;

import com.kozubek.userdomain.core.Role;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class RoleEntityCommandMapper {

    public Set<Role> roleEntitiesToRoles(Collection<RoleEntity> roleEntities) {
        Set<Role> roles = new HashSet<>();
        for (RoleEntity roleEntity : roleEntities) {
            roles.add(roleEntityToRole(roleEntity));
        }
        return roles;
    }

    private Role roleEntityToRole(RoleEntity roleEntity) {
        return new Role(roleEntity.getId(), roleEntity.getName());
    }

    public Set<RoleEntity> rolesToRoleEntities(Set<Role> roles) {
        Set<RoleEntity> roleEntities = new HashSet<>();
        for (Role role : roles) {
            roleEntities.add(roleToRoleEntity(role));
        }
        return roleEntities;
    }

    private RoleEntity roleToRoleEntity(Role role) {
        return RoleEntity.builder()
                .id(role.getId().id())
                .name(role.getName())
                .build();
    }
}
