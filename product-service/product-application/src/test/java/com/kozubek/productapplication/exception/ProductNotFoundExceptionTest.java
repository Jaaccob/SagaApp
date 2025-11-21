package com.kozubek.productapplication.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductNotFoundException Tests")
class ProductNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // given
        final String message = "Product not found";

        // when
        final ProductNotFoundException exception = new ProductNotFoundException(message);

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @DisplayName("Should create exception with various messages")
    @ValueSource(strings = {
            "Product not found",
            "Product with ID 123e4567-e89b-12d3-a456-426614174000 not found",
            "Product not found in database",
            "No product exists with the given ID"
    })
    void shouldCreateExceptionWithVariousMessages(String message) {
        // given & when
        final ProductNotFoundException exception = new ProductNotFoundException(message);

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should create exception with product ID in message")
    void shouldCreateExceptionWithProductIdInMessage() {
        // given
        final UUID productId = UUID.randomUUID();
        final String message = "Product with ID " + productId + " not found";

        // when
        final ProductNotFoundException exception = new ProductNotFoundException(message);

        // then
        assertThat(exception.getMessage()).contains(productId.toString());
        assertThat(exception.getMessage()).contains("not found");
    }

    @ParameterizedTest
    @DisplayName("Should handle null or empty messages")
    @NullAndEmptySource
    void shouldHandleNullOrEmptyMessages(String message) {
        // given & when
        final ProductNotFoundException exception = new ProductNotFoundException(message);

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should be throwable as RuntimeException")
    void shouldBeThrowableAsRuntimeException() {
        // given
        final String message = "Product not found";

        // when
        final ProductNotFoundException exception = new ProductNotFoundException(message);

        // then
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Should preserve message after being thrown and caught")
    void shouldPreserveMessageAfterBeingThrownAndCaught() {
        // given
        final String expectedMessage = "Product with code PROD-001 not found";

        // when
        try {
            throw new ProductNotFoundException(expectedMessage);
        } catch (ProductNotFoundException e) {
            // then
            assertThat(e.getMessage()).isEqualTo(expectedMessage);
        }
    }
}
