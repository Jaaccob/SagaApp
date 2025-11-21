package com.kozubek.productadapters.rest;

import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.productadapters.rest.dto.GetDetailsProductResponse;
import com.kozubek.productapplication.query.dto.ProductProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductQueryRestMapper Unit Tests")
class ProductQueryRestMapperTest {

    private ProductQueryRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductQueryRestMapper();
    }

    @Test
    @DisplayName("Should map ProductProjection to GetDetailsProductResponse with all fields preserved")
    void shouldMapProductProjectionToGetDetailsProductResponse() {
        // given
        final UUID productId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final String code = "PROD-001";
        final String name = "Test Product";
        final BigDecimal price = BigDecimal.valueOf(99.99);
        final int quantity = 50;
        final ProductStatus status = ProductStatus.AVAILABLE;

        final ProductProjection projection = ProductProjection.builder()
                .productId(productId)
                .userId(userId)
                .code(code)
                .name(name)
                .price(price)
                .quantity(quantity)
                .status(status)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(productId);
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.code()).isEqualTo(code);
        assertThat(response.name()).isEqualTo(name);
        assertThat(response.price()).isEqualByComparingTo(price);
        assertThat(response.quantity()).isEqualTo(quantity);
        assertThat(response.status()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should preserve productId when mapping")
    void shouldPreserveProductId() {
        // given
        final UUID expectedProductId = UUID.randomUUID();
        final ProductProjection projection = ProductProjection.builder()
                .productId(expectedProductId)
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.productId()).isEqualTo(expectedProductId);
    }

    @Test
    @DisplayName("Should preserve userId when mapping")
    void shouldPreserveUserId() {
        // given
        final UUID expectedUserId = UUID.randomUUID();
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(expectedUserId)
                .code("CODE")
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.userId()).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("Should preserve status when mapping AVAILABLE")
    void shouldPreserveStatusAvailable() {
        // given
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.status()).isEqualTo(ProductStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Should preserve status when mapping NOT_AVAILABLE")
    void shouldPreserveStatusNotAvailable() {
        // given
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(5)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.status()).isEqualTo(ProductStatus.NOT_AVAILABLE);
    }

    @Test
    @DisplayName("Should preserve code when mapping")
    void shouldPreserveCode() {
        // given
        final String expectedCode = "SPECIAL-CODE-XYZ";
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code(expectedCode)
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Should preserve name when mapping")
    void shouldPreserveName() {
        // given
        final String expectedName = "Product with Special Characters !@# 123";
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name(expectedName)
                .price(BigDecimal.TEN)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.name()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Should preserve price precision when mapping")
    void shouldPreservePricePrecision() {
        // given
        final BigDecimal expectedPrice = new BigDecimal("1234.5678");
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(expectedPrice)
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.price()).isEqualByComparingTo(expectedPrice);
    }

    @Test
    @DisplayName("Should preserve quantity when mapping")
    void shouldPreserveQuantity() {
        // given
        final int expectedQuantity = 789;
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.TEN)
                .quantity(expectedQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.quantity()).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("Should handle minimum quantity value")
    void shouldHandleMinimumQuantity() {
        // given
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.ONE)
                .quantity(1)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.quantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle large quantity value")
    void shouldHandleLargeQuantity() {
        // given
        final int largeQuantity = 999999;
        final ProductProjection projection = ProductProjection.builder()
                .productId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .code("CODE")
                .name("Name")
                .price(BigDecimal.ONE)
                .quantity(largeQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();

        // when
        final GetDetailsProductResponse response = mapper.productToGetDetailsProductResponse(projection);

        // then
        assertThat(response.quantity()).isEqualTo(largeQuantity);
    }
}
