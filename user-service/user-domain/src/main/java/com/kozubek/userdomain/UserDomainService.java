package com.kozubek.userdomain;

import com.kozubek.userdomain.core.Role;
import com.kozubek.userdomain.core.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserDomainService {

    public User createUser(String userName, String password, String email) {
        final Role role = new Role("ROLE_USER");
        return new User(userName, password, email, Set.of(role));
    }
}
