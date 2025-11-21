package com.kozubek.productdomain.event;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.core.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductCreatedEvent Tests")
class ProductCreatedEventTest {

    @Test
    @DisplayName("Should create ProductCreatedEvent with product and timestamp")
    void shouldCreateProductCreatedEvent() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("P-200")
                .name("Event Product")
                .price(new Money(BigDecimal.valueOf(20)))
                .quantity(15)
                .status(ProductStatus.AVAILABLE)
                .build();
        final Instant now = Instant.now();

        // when
        final ProductCreatedEvent event = new ProductCreatedEvent(product, now);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct()).isEqualTo(product);
        assertThat(event.getCreatedAt()).isEqualTo(now);
        assertThat(event.getProduct().getId()).isEqualTo(product.getId());
        assertThat(event.getProduct().getCode()).isEqualTo("P-200");
        assertThat(event.getProduct().getName()).isEqualTo("Event Product");
        assertThat(event.getProduct().getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(20.00));
        assertThat(event.getProduct().getQuantity()).isEqualTo(15);
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should preserve product reference in event")
    void shouldPreserveProductReference() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("P-201")
                .name("Reference Product")
                .price(new Money(BigDecimal.valueOf(100)))
                .quantity(20)
                .status(ProductStatus.AVAILABLE)
                .build();
        final Instant timestamp = Instant.now();

        // when
        final ProductCreatedEvent event = new ProductCreatedEvent(product, timestamp);

        // then
        assertThat(event.getProduct()).isSameAs(product);
        assertThat(event.getCreatedAt()).isSameAs(timestamp);
    }

    @Test
    @DisplayName("Should create event with past timestamp")
    void shouldCreateEventWithPastTimestamp() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("P-202")
                .name("Past Event Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(25)
                .status(ProductStatus.AVAILABLE)
                .build();
        final Instant pastTimestamp = Instant.now().minusSeconds(3600);

        // when
        final ProductCreatedEvent event = new ProductCreatedEvent(product, pastTimestamp);

        // then
        assertThat(event.getCreatedAt()).isEqualTo(pastTimestamp);
        assertThat(event.getCreatedAt()).isBefore(Instant.now());
    }

    @Test
    @DisplayName("Should create event for product with NOT_AVAILABLE status")
    void shouldCreateEventForNotAvailableProduct() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("P-203")
                .name("Out of Stock Product")
                .price(new Money(BigDecimal.valueOf(30)))
                .quantity(5)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();
        final Instant timestamp = Instant.now();

        // when
        final ProductCreatedEvent event = new ProductCreatedEvent(product, timestamp);

        // then
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.NOT_AVAILABLE);
        assertThat(event.getCreatedAt()).isEqualTo(timestamp);
    }
}

