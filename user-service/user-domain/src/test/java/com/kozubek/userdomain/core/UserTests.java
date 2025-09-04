package com.kozubek.userdomain.core;

import com.kozubek.userdomain.exceptions.UserDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User domain validation tests")
class UserTests {

    private static final Role role = new Role(UUID.randomUUID(), "ROLE_USER");

    @Test
    void shouldValidateSuccessfullyForRegisteredUser() {
        User user = User.builder()
                .username("test123")
                .password("Test123$")
                .email("test@mail.com")
                .roles(Set.of(role))
                .build();

        assertDoesNotThrow(user::validateForRegistration);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsNull() {
        User user = User.builder()
                .username(null)
                .password("Test123$")
                .email("test@mail.com")
                .roles(Set.of(role))
                .build();

        assertThrows(UserDomainException.class, user::validateForRegistration);
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsTooShort() {
        User user = User.builder()
                .username("test")
                .password("Test123$")
                .email("test@mail.com")
                .roles(Set.of(role))
                .build();

        UserDomainException exception = assertThrows(UserDomainException.class, user::validateForRegistration);
        assertEquals("Username must be at least 6 characters", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUsernameIsTooLong() {
        User user = User.builder()
                .username("test-test-test-test-test-test-test-test-test-test")
                .password("Test123$")
                .email("test@mail.com")
                .roles(Set.of(role))
                .build();

        UserDomainException exception = assertThrows(UserDomainException.class, user::validateForRegistration);
        assertEquals("Username is too long", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        User user = User.builder()
                .username("test123")
                .password("test")
                .email("test@mail.com")
                .roles(Set.of(role))
                .build();

        UserDomainException ex = assertThrows(UserDomainException.class, user::validateForRegistration);
        assertEquals("Password must be at least 6 characters", ex.getMessage());
    }

}
