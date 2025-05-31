package com.kozubek.userdomain.core;

import com.kozubek.commondomain.model.AggregateRoot;
import com.kozubek.commondomain.vo.UserId;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class User implements AggregateRoot {
    private final UserId id;
    private final String username;
    private final String password;
    private final String email;
    private final Set<Role> roles;

    public User(final String username, String password, final String email, final Set<Role> roles) {
        this.id = new UserId(UUID.randomUUID());
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    public User(final UUID id, final String username, String password, final String email, final Set<Role> roles) {
        this.id = new UserId(id);
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
}
