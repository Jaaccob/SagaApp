package com.kozubek.productdomain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductDomainException Tests")
class ProductDomainExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        // given
        final String message = "Product validation failed";

        // when
        final ProductDomainException exception = new ProductDomainException(message);

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @ParameterizedTest
    @DisplayName("Should create exception with various messages")
    @ValueSource(strings = {
            "Product price: 0 must be greater than zero",
            "Product quantity: -1 must be greater than zero",
            "Product quantity: 5 must be greater than 10"
    })
    void shouldCreateExceptionWithVariousMessages(final String message) {
        // given & when
        final ProductDomainException exception = new ProductDomainException(message);

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @ParameterizedTest
    @DisplayName("Should handle null or empty messages")
    @NullAndEmptySource
    void shouldHandleNullOrEmptyMessages(final String message) {
        // given & when
        final ProductDomainException exception = new ProductDomainException(message);

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}
