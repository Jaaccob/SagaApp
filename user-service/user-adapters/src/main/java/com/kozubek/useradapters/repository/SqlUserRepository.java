package com.kozubek.useradapters.repository;

import com.kozubek.userdomain.core.User;
import com.kozubek.userdomain.port.UserRepository;
import com.kozubek.userentities.UserEntity;
import com.kozubek.userentities.UserEntityCommandMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class SqlUserRepository implements UserRepository {
    private final UserRepositoryJpa userRepositoryJpa;
    private final UserEntityCommandMapper userEntityCommandMapper;

    public SqlUserRepository(UserRepositoryJpa userRepositoryJpa, UserEntityCommandMapper userEntityCommandMapper) {
        this.userRepositoryJpa = userRepositoryJpa;
        this.userEntityCommandMapper = userEntityCommandMapper;
    }

    public User findByUsername(String username) {
        UserEntity userEntity = userRepositoryJpa.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userEntityCommandMapper.userEntityToUser(userEntity);
    }

    public void existsByUsernameAndThrowException(String username) {
        boolean exists = userRepositoryJpa.existsByUsername(username);
        if (exists) {
            throw new RuntimeException("User is exec in database");
        }
    }

    public User save(User user) {
        return userEntityCommandMapper.userEntityToUser(userRepositoryJpa.save(userEntityCommandMapper.userToUserEntity(user)));
    }
}

@Repository
interface UserRepositoryJpa extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}