package com.kozubek.userdomain.events;

import com.kozubek.userdomain.core.User;

import java.time.Instant;

public class UserLoggedEvent extends UserEvent {

    public UserLoggedEvent(User user, Instant createdAt) {
        super(user, createdAt);
    }
}
