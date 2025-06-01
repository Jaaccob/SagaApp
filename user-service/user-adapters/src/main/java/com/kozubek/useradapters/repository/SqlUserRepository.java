package com.kozubek.useradapters.repository;

import com.kozubek.userdomain.core.User;
import com.kozubek.userdomain.port.UserRepository;
import com.kozubek.userentities.UserEntity;
import com.kozubek.userentities.UserEntityCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlUserRepository implements UserRepository {
    private final UserRepositoryJpa repository;
    private final UserEntityCommandMapper commandMapper;

    public User findByUsername(final String username) {
        UserEntity userEntity = repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return commandMapper.userEntityToUser(userEntity);
    }

    public void existsByUsernameAndThrowException(final String username) {
        boolean exists = repository.existsByUsername(username);
        if (exists) {
            throw new RuntimeException("User is exec in database");
        }
    }

    public User save(User user) {
        return commandMapper.userEntityToUser(repository.save(commandMapper.userToUserEntity(user)));
    }
}

@Repository
interface UserRepositoryJpa extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(final String username);

    boolean existsByUsername(final String username);
}