package com.kozubek.productdomain;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import com.kozubek.productdomain.exception.ProductDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProductDomainService Tests")
class ProductDomainServiceTest {

    private ProductDomainService productDomainService;

    @BeforeEach
    void setUp() {
        productDomainService = new ProductDomainService();
    }

    @Test
    @DisplayName("Should create product, initialize it, validate and return ProductCreatedEvent")
    void shouldCreateProductAndReturnEvent() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-100")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(15)
                .build();
        final Instant beforeCreation = Instant.now();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct()).isEqualTo(product);
        assertThat(event.getProduct().getId()).isNotNull();
        assertThat(event.getProduct().getId().id()).isNotNull();
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
        assertThat(event.getProduct().getName()).isEqualTo("Test Product");
        assertThat(event.getProduct().getCode()).isEqualTo("P-100");
        assertThat(event.getProduct().getQuantity()).isEqualTo(15);
        assertThat(event.getProduct().getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(event.getCreatedAt()).isNotNull();
        assertThat(event.getCreatedAt()).isAfterOrEqualTo(beforeCreation);
    }

    @Test
    @DisplayName("Should throw exception when creating product with invalid price")
    void shouldThrowExceptionWhenCreatingProductWithInvalidPrice() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-101")
                .name("Invalid Product")
                .price(new Money(BigDecimal.ZERO))
                .quantity(15)
                .build();

        // when & then
        assertThatThrownBy(() -> productDomainService.create(product))
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("must be greater than zero");
    }

    @Test
    @DisplayName("Should throw exception when creating product with invalid quantity")
    void shouldThrowExceptionWhenCreatingProductWithInvalidQuantity() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-102")
                .name("Invalid Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(0)
                .build();

        // when & then
        assertThatThrownBy(() -> productDomainService.create(product))
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than zero");
    }

    @Test
    @DisplayName("Should throw exception when creating product with quantity less than 10")
    void shouldThrowExceptionWhenCreatingProductWithQuantityLessThanTen() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-103")
                .name("Invalid Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(5)
                .build();

        // when & then
        assertThatThrownBy(() -> productDomainService.create(product))
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("quantity")
                .hasMessageContaining("must be greater than 10");
    }

    @Test
    @DisplayName("Should create product with minimum valid quantity")
    void shouldCreateProductWithMinimumValidQuantity() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-104")
                .name("Boundary Product")
                .price(new Money(BigDecimal.valueOf(50)))
                .quantity(11)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct().getQuantity()).isEqualTo(11);
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should create product with minimum valid price")
    void shouldCreateProductWithMinimumValidPrice() {
        // given
        final Product product = Product.builder()
                .userId(new UserId(UUID.randomUUID()))
                .code("P-105")
                .name("Cheap Product")
                .price(new Money(BigDecimal.valueOf(0.01)))
                .quantity(15)
                .build();

        // when
        final ProductCreatedEvent event = productDomainService.create(product);

        // then
        assertThat(event).isNotNull();
        assertThat(event.getProduct().getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(0.01));
        assertThat(event.getProduct().getStatus()).isEqualTo(ProductStatus.AVAILABLE);
    }
}

