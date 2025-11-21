package com.kozubek.productdomain.property;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.ProductDomainService;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 100)
class ProductDomainServicePropertyTest {

    private final ProductDomainService productDomainService = new ProductDomainService();

    @Property
    @Label("Created product always has an ID after creation")
    void createdProductAlwaysHasId(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("DOMAIN-001")
                .name("Domain Service Test")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event.getProduct().getId()).isNotNull();
        assertThat(event.getProduct().getId().id()).isNotNull();
    }

    @Property
    @Label("Created product always has AVAILABLE status after creation")
    void createdProductAlwaysHasAvailableStatus(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("DOMAIN-002")
                .name("Domain Service Test")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Property
    @Label("ProductCreatedEvent always has a valid timestamp")
    void productCreatedEventAlwaysHasValidTimestamp(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("DOMAIN-003")
                .name("Domain Service Test")
                .price(new Money(price))
                .quantity(quantity)
                .build();
        final Instant beforeCreation = Instant.now();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        final Instant afterCreation = Instant.now();
        assertThat(event.getCreatedAt()).isNotNull();
        assertThat(event.getCreatedAt()).isBetween(beforeCreation.minusSeconds(1), afterCreation.plusSeconds(1));
    }

    @Property
    @Label("ProductCreatedEvent preserves all product properties")
    void productCreatedEventPreservesAllProperties(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity,
            @ForAll("productCodes") final String code,
            @ForAll("productNames") final String name
    ) {
        // given
        final UserId userId = new UserId(UUID.randomUUID());
        final Product product = Product.builder()
                .userId(userId)
                .code(code)
                .name(name)
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event.getProduct().getUserId()).isEqualTo(userId);
        assertThat(event.getProduct().getCode()).isEqualTo(code);
        assertThat(event.getProduct().getName()).isEqualTo(name);
        assertThat(event.getProduct().getPrice().amount()).isEqualByComparingTo(price);
        assertThat(event.getProduct().getQuantity()).isEqualTo(quantity);
    }

    @Property
    @Label("Multiple creations produce different product IDs")
    void multipleCreationsProduceDifferentProductIds(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product1 = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("MULTI-001")
                .name("Multi Creation Test 1")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        final Product product2 = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("MULTI-002")
                .name("Multi Creation Test 2")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event1 = productDomainService.create(product1);
        final ProductCreatedEvent event2 = productDomainService.create(product2);

        // then
        assertThat(event1.getProduct().getId()).isNotEqualTo(event2.getProduct().getId());
    }

    @Property
    @Label("Creation with boundary price values succeeds")
    void creationWithBoundaryPriceValuesSucceeds(
            @ForAll("boundaryPrices") final BigDecimal boundaryPrice,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("BOUNDARY-PRICE")
                .name("Boundary Price Test")
                .price(new Money(boundaryPrice))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct().getPrice().amount()).isPositive();
    }

    @Property
    @Label("Creation with boundary quantity values succeeds")
    void creationWithBoundaryQuantityValuesSucceeds(
            @ForAll @Positive final BigDecimal price,
            @ForAll("boundaryQuantities") final int boundaryQuantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("BOUNDARY-QTY")
                .name("Boundary Quantity Test")
                .price(new Money(price))
                .quantity(boundaryQuantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct().getQuantity()).isGreaterThan(10);
    }

    @Property
    @Label("ProductCreatedEvent contains same product instance")
    void productCreatedEventContainsSameProductInstance(
            @ForAll @Positive final BigDecimal price,
            @ForAll @IntRange(min = 11, max = 10000) final int quantity
    ) {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("INSTANCE-TEST")
                .name("Instance Test")
                .price(new Money(price))
                .quantity(quantity)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event.getProduct()).isSameAs(product);
    }

    // Providers
    @Provide
    Arbitrary<String> productCodes() {
        return Arbitraries.strings()
                .withCharRange('A', 'Z')
                .numeric()
                .withChars('-', '_')
                .ofMinLength(3)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<String> productNames() {
        return Arbitraries.strings()
                .alpha()
                .numeric()
                .withChars(' ', '-', '_', '.')
                .ofMinLength(5)
                .ofMaxLength(100);
    }

    @Provide
    Arbitrary<BigDecimal> boundaryPrices() {
        return Arbitraries.of(
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(0.99),
                BigDecimal.valueOf(1.00),
                BigDecimal.valueOf(9.99),
                BigDecimal.valueOf(10.00),
                BigDecimal.valueOf(99.99),
                BigDecimal.valueOf(100.00),
                BigDecimal.valueOf(999.99),
                BigDecimal.valueOf(1000.00)
        );
    }

    @Provide
    Arbitrary<Integer> boundaryQuantities() {
        return Arbitraries.of(11, 12, 50, 100, 999, 1000, 9999, 10000);
    }
}
