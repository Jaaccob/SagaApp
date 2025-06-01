package com.kozubek.userdomain.port;

import com.kozubek.userdomain.core.User;

public interface UserRepository {

    User findByUsername(String username);
    void existsByUsernameAndThrowException(String username);
    User save(User user);
}
