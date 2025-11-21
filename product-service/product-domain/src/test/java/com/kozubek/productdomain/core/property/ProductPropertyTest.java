package com.kozubek.productdomain.core.property;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.exception.ProductDomainException;
import net.jqwik.api.*;
import net.jqwik.api.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@PropertyDefaults(tries = 100)
class ProductPropertyTest {

    @Property
    @Label("Price must always be positive after validation")
    void priceAlwaysPositiveAfterValidation(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-001")
                .name("Property Test Product")
                .price(new Money(price))
                .quantity(quantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then
        assertThat(product.getPrice().amount()).isPositive();
    }

    @Property
    @Label("Quantity must be greater than 10 for AVAILABLE status")
    void quantityMustBeGreaterThan10ForAvailableStatus(
            @ForAll @IntRange(min = 1, max = 9) final int invalidQuantity,
            @ForAll @Positive final BigDecimal price
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-002")
                .name("Property Test Product")
                .price(new Money(price))
                .quantity(invalidQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validateQuantity)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than 10");
    }

    @Property
    @Label("Quantity can be less than 10 for NOT_AVAILABLE status")
    void quantityCanBeLessThan10ForNotAvailableStatus(
            @ForAll @IntRange(min = 1, max = 9) final int validQuantity,
            @ForAll @Positive final BigDecimal price
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-003")
                .name("Property Test Product")
                .price(new Money(price))
                .quantity(validQuantity)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();

        // when
        product.validateQuantity();

        // then - no exception thrown
        assertThat(product.getQuantity()).isEqualTo(validQuantity);
    }

    @Property
    @Label("Zero or negative price should always fail validation")
    void zeroOrNegativePriceShouldAlwaysFail(
            @ForAll @BigRange(max = "0") final BigDecimal invalidPrice,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-004")
                .name("Invalid Price Product")
                .price(new Money(invalidPrice))
                .quantity(quantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when & then
        assertThatThrownBy(product::validatePrice)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("must be greater than zero");
    }

    @Property
    @Label("Initialize always sets status to AVAILABLE")
    void initializeAlwaysSetsStatusToAvailable(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity,
            @ForAll("productStatuses") final ProductStatus initialStatus
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-005")
                .name("Initialize Test Product")
                .price(new Money(price))
                .quantity(quantity)
                .status(initialStatus)
                .build();

        // when
        product.initialize();

        // then
        assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
        assertThat(product.getId()).isNotNull();
    }

    @Property
    @Label("Initialize always generates non-null product ID")
    void initializeAlwaysGeneratesNonNullId(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("PROP-006")
                .name("ID Generation Test")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        product.initialize();

        // then
        assertThat(product.getId()).isNotNull();
        assertThat(product.getId().id()).isNotNull();
    }

    @Property
    @Label("Product code preserves all valid characters")
    void productCodePreservesAllValidCharacters(
            @ForAll @AlphaChars @StringLength(min = 1, max = 50) final String code,
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given & when
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code(code)
                .name("Code Test Product")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // then
        assertThat(product.getCode()).isEqualTo(code);
    }

    @Property
    @Label("Product name preserves all valid characters")
    void productNamePreservesAllValidCharacters(
            @ForAll @StringLength(min = 1, max = 200) final String name,
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given & when
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("NAME-TEST")
                .name(name)
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // then
        assertThat(product.getName()).isEqualTo(name);
    }

    @Property
    @Label("isAvailableStatus returns true only for AVAILABLE status")
    void isAvailableStatusReturnsTrueOnlyForAvailable(
            @ForAll("productStatuses") final ProductStatus status,
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("STATUS-TEST")
                .name("Status Test Product")
                .price(new Money(price))
                .quantity(quantity)
                .status(status)
                .build();

        // when
        final boolean result = product.isAvailableStatus();

        // then
        if (status == ProductStatus.AVAILABLE) {
            assertThat(result).isTrue();
        } else {
            assertThat(result).isFalse();
        }
    }

    @Property
    @Label("Validate succeeds for all valid combinations")
    void validateSucceedsForValidCombinations(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("VALID-TEST")
                .name("Valid Product")
                .price(new Money(price))
                .quantity(quantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then - no exception thrown
        assertThat(product.getPrice().amount()).isPositive();
        assertThat(product.getQuantity()).isGreaterThan(10);
    }

    @Property
    @Label("Money amount is always scaled to 2 decimal places")
    void moneyAmountIsAlwaysScaledTo2DecimalPlaces(
            @ForAll @Positive @Scale(value = 5) final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Money money = new Money(price);
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("SCALE-TEST")
                .name("Scale Test Product")
                .price(money)
                .quantity(quantity)
                .build();

        // when & then
        assertThat(product.getPrice().amount().scale()).isEqualTo(2);
    }

    @Property
    @Label("Quantity less than 1 always fails validation")
    void quantityLessThan1AlwaysFails(
            @ForAll @IntRange(max = 0) final int invalidQuantity,
            @ForAll @Positive final BigDecimal price
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("QTY-FAIL-TEST")
                .name("Quantity Fail Test")
                .price(new Money(price))
                .quantity(invalidQuantity)
                .build();

        // when & then
        assertThatThrownBy(product::validateQuantity)
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than zero");
    }

    @Property
    @Label("Large quantities are handled correctly")
    void largeQuantitiesAreHandledCorrectly(
            @ForAll @IntRange(min = 100000, max = Integer.MAX_VALUE - 1) final int largeQuantity,
            @ForAll @Positive final BigDecimal price
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("LARGE-QTY")
                .name("Large Quantity Product")
                .price(new Money(price))
                .quantity(largeQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then
        assertThat(product.getQuantity()).isEqualTo(largeQuantity);
    }

    @Property
    @Label("Large prices are handled correctly")
    void largePricesAreHandledCorrectly(
            @ForAll @BigRange(min = "1000000", max = "999999999999.99") final BigDecimal largePrice,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("LARGE-PRICE")
                .name("Large Price Product")
                .price(new Money(largePrice))
                .quantity(quantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then
        assertThat(product.getPrice().amount()).isGreaterThanOrEqualTo(BigDecimal.valueOf(1000000));
    }

    @Property
    @Label("Very small positive prices are handled correctly")
    void verySmallPositivePricesAreHandledCorrectly(
            @ForAll @BigRange(min = "0.01", max = "0.99") final BigDecimal smallPrice,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("SMALL-PRICE")
                .name("Small Price Product")
                .price(new Money(smallPrice))
                .quantity(quantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        product.validate();

        // then
        assertThat(product.getPrice().amount()).isPositive();
        assertThat(product.getPrice().amount()).isLessThan(BigDecimal.ONE);
    }

    // Providers
    @Provide
    Arbitrary<ProductStatus> productStatuses() {
        return Arbitraries.of(ProductStatus.class);
    }
}
