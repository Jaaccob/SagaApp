package com.kozubek.userdomain.events;

import com.kozubek.userdomain.core.User;

import java.time.Instant;

public class UserCreatedEvent extends UserEvent {

    public UserCreatedEvent(User user, Instant createdAt) {
        super(user, createdAt);
    }
}
