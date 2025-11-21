package com.kozubek.productdomain.core;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.exception.ProductDomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product Aggregate Root Tests")
class ProductTest {

    @Test
    @DisplayName("Should initialize product with ID and AVAILABLE status")
    void shouldInitializeProduct() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-001")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(100)))
                .quantity(15)
                .build();

        // when
        product.initialize();

        // then
        assertThat(product.getId()).isNotNull();
        assertThat(product.getId().id()).isNotNull();
        assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when price is not greater than zero")
    @MethodSource("invalidPriceProvider")
    void shouldThrowExceptionWhenPriceIsInvalid(BigDecimal invalidPrice, String description) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-002")
                .name("Test Product")
                .price(new Money(invalidPrice))
                .quantity(15)
                .build();
        product.initialize();

        // when & then
        assertThatThrownBy(product::validatePrice)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("must be greater than zero");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when quantity is less than 1")
    @ValueSource(ints = {0, -1, -5})
    void shouldThrowExceptionWhenQuantityIsLessThanOne(int invalidQuantity) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-003")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(10)))
                .quantity(invalidQuantity)
                .build();
        product.initialize();

        // when & then
        assertThatThrownBy(product::validateQuantity)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than zero");
    }

    @ParameterizedTest
    @DisplayName("Should throw exception when quantity is less than 10 and status is AVAILABLE")
    @ValueSource(ints = {1, 5, 9})
    void shouldThrowExceptionWhenQuantityIsLessThanTenForAvailableStatus(int invalidQuantity) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-004")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(10)))
                .quantity(invalidQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validateQuantity)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than 10");
    }

    @ParameterizedTest
    @DisplayName("Should not throw exception when quantity is less than 10 but status is not AVAILABLE")
    @ValueSource(ints = {1, 5, 9})
    void shouldNotThrowExceptionWhenQuantityIsLessThanTenForNonAvailableStatus(int validQuantity) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-005")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(10)))
                .quantity(validQuantity)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();

        // when
        product.validateQuantity();

        // then - no exception thrown
        assertThat(product.getQuantity()).isEqualTo(validQuantity);
    }

    @Test
    @DisplayName("Should validate entire product successfully")
    void shouldValidateProductSuccessfully() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-006")
                .name("Valid Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(20)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then - no exception thrown
        assertThat(product.getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(product.getQuantity()).isEqualTo(20);
    }

    @Test
    @DisplayName("Should throw exception when validating product with invalid price")
    void shouldThrowExceptionWhenValidatingProductWithInvalidPrice() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-007")
                .name("Invalid Product")
                .price(new Money(BigDecimal.ZERO))
                .quantity(20)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validate)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("price");
    }

    @Test
    @DisplayName("Should throw exception when validating product with invalid quantity")
    void shouldThrowExceptionWhenValidatingProductWithInvalidQuantity() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-008")
                .name("Invalid Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(5)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validate)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity");
    }

    @ParameterizedTest
    @DisplayName("Should correctly identify AVAILABLE status")
    @MethodSource("productStatusProvider")
    void shouldCheckIfProductIsAvailable(ProductStatus status, boolean expectedResult) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-009")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(10)))
                .quantity(15)
                .status(status)
                .build();

        // when
        final boolean result = product.isAvailableStatus();

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("Should validate product with valid price at boundary")
    void shouldValidateProductWithValidPriceAtBoundary() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-010")
                .name("Boundary Product")
                .price(new Money(BigDecimal.valueOf(0.01)))
                .quantity(15)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validatePrice();

        // then - no exception thrown
        assertThat(product.getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(0.01));
    }

    @Test
    @DisplayName("Should validate product with quantity exactly 10 and AVAILABLE status")
    void shouldValidateProductWithQuantityTenAndAvailableStatus() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-011")
                .name("Boundary Product")
                .price(new Money(BigDecimal.valueOf(10)))
                .quantity(9)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validateQuantity)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("must be greater than 10");
    }

    // Test data providers
    private static Stream<Arguments> invalidPriceProvider() {
        return Stream.of(
                Arguments.of(BigDecimal.ZERO, "Zero price"),
                Arguments.of(BigDecimal.valueOf(-1), "Negative price"),
                Arguments.of(BigDecimal.valueOf(-100.50), "Large negative price")
        );
    }

    private static Stream<Arguments> productStatusProvider() {
        return Stream.of(
                Arguments.of(ProductStatus.AVAILABLE, true),
                Arguments.of(ProductStatus.NOT_AVAILABLE, false)
        );
    }
}

