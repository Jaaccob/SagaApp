package com.kozubek.userdomain.events;

import com.kozubek.commondomain.event.DomainEvent;
import com.kozubek.userdomain.core.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
public abstract class UserEvent implements DomainEvent<User> {

    private final User user;
    private final Instant createdAt;
}
