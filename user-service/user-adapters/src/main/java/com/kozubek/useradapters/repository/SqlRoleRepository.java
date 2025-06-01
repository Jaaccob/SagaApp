package com.kozubek.useradapters.repository;

import com.kozubek.userdomain.core.Role;
import com.kozubek.userdomain.port.RoleRepository;
import com.kozubek.userentities.RoleEntity;
import com.kozubek.userentities.RoleEntityCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlRoleRepository implements RoleRepository {
    private final RoleRepositoryJpa repository;
    private final RoleEntityCommandMapper commandMapper;

    public Set<Role> findAll() {
        return commandMapper.roleEntitiesToRoles(repository.findAll());
    }

}

@Repository
interface RoleRepositoryJpa extends JpaRepository<RoleEntity, UUID> {}