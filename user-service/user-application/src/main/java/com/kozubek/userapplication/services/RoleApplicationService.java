package com.kozubek.userapplication.services;

import com.kozubek.commonapplication.enums.SystemRole;
import com.kozubek.userdomain.core.Role;
import com.kozubek.userdomain.port.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RoleApplicationService {

    private final RoleRepository roleRepository;
    private Map<SystemRole, Role> roles;

    public RoleApplicationService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.roles = new HashMap<>();
    }

    public Role getRoleFromCache(SystemRole role) {
        if (!roles.containsKey(role)) {
            updateCache();
        }
        return roles.get(role);
    }

    private void updateCache() {
        roles = roleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(role -> SystemRole.valueOf(role.getName()), Function.identity()));
    }
}
