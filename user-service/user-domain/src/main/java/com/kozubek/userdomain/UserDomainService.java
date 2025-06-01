package com.kozubek.userdomain;

import com.kozubek.userdomain.core.User;
import com.kozubek.userdomain.events.UserCreatedEvent;
import com.kozubek.userdomain.events.UserLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDomainService {

    public UserCreatedEvent createUser(User user) {
        user.validateForRegistration();
        return new UserCreatedEvent(user, Instant.now());
    }

    public UserLoggedEvent logUser(User user) {
        user.validateForLogin();
        return new UserLoggedEvent(user, Instant.now());
    }
}
