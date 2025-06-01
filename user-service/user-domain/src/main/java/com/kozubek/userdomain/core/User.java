package com.kozubek.userdomain.core;

import com.kozubek.commondomain.model.AggregateRoot;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.userdomain.exceptions.UserDomainException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Builder
@Getter
public class User implements AggregateRoot {
    private final UserId id;
    private final String username;
    private final String password;
    private final String email;
    private final Set<Role> roles;

    public void validateForRegistration() { //todo fix validation to use change design pattern
        validateName();
        validatePassword();
        validateEmail();
    }

    public void validateForLogin() {
        validateName();
        validatePassword();
    }

    private void validateName() {
        if (ObjectUtils.isEmpty(username)) {
            throw new UserDomainException("Username is required");
        } else if (username.length() < 6) {
            throw new UserDomainException("Username must be at least 6 characters");
        } else if (username.length() > 20) {
            throw new UserDomainException("Username is too long");
        }
    }

    private void validatePassword() {
        if (ObjectUtils.isEmpty(password)) {
            throw new UserDomainException("Password is required");
        } else if (password.length() < 6) {
            throw new UserDomainException("Password must be at least 6 characters");
        } else if (password.contains("<") || password.contains(">")) {
            throw new UserDomainException("Password contains '<>'");
        }

        boolean hasSpecialCharacters = password.chars()
                .anyMatch(character -> !Character.isLetterOrDigit(character));
        if (!hasSpecialCharacters) {
            throw new UserDomainException("Password must contain at least one character");
        }

        boolean hasUpperCharacters = password.chars()
                .anyMatch(Character::isUpperCase);

        if (!hasUpperCharacters) {
            throw new UserDomainException("Password must contain at least one upper character");
        }
    }

    private void validateEmail() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (ObjectUtils.isEmpty(email)) {
            throw new UserDomainException("Email is required");
        } else if (!email.matches(emailRegex)) {
            throw new UserDomainException("The email address provided is incorrect");
        }
    }
}
