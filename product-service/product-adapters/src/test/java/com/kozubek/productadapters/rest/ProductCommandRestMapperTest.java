package com.kozubek.productadapters.rest;

import com.kozubek.productadapters.rest.dto.CreateProductRequest;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductCommandRestMapper Unit Tests")
class ProductCommandRestMapperTest {

    private ProductCommandRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductCommandRestMapper();
    }

    @Test
    @DisplayName("Should map CreateProductRequest to CreateProductCommand with all fields preserved")
    void shouldMapCreateProductRequestToCreateProductCommand() {
        // given
        final UUID userId = UUID.randomUUID();
        final String code = "PROD-001";
        final String name = "Test Product";
        final BigDecimal price = BigDecimal.valueOf(99.99);
        final int quantity = 50;

        final CreateProductRequest request = new CreateProductRequest(
                userId,
                code,
                name,
                price,
                quantity
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command).isNotNull();
        assertThat(command.userId()).isEqualTo(userId);
        assertThat(command.code()).isEqualTo(code);
        assertThat(command.name()).isEqualTo(name);
        assertThat(command.price()).isEqualByComparingTo(price);
        assertThat(command.quantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should preserve userId when mapping")
    void shouldPreserveUserId() {
        // given
        final UUID expectedUserId = UUID.randomUUID();
        final CreateProductRequest request = new CreateProductRequest(
                expectedUserId,
                "CODE",
                "Name",
                BigDecimal.TEN,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.userId()).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("Should preserve code when mapping")
    void shouldPreserveCode() {
        // given
        final String expectedCode = "SPECIAL-CODE-123";
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                expectedCode,
                "Name",
                BigDecimal.TEN,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Should preserve name when mapping")
    void shouldPreserveName() {
        // given
        final String expectedName = "Super Product with Special Characters !@#";
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                expectedName,
                BigDecimal.TEN,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.name()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Should preserve price precision when mapping")
    void shouldPreservePricePrecision() {
        // given
        final BigDecimal expectedPrice = new BigDecimal("123.4567");
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                expectedPrice,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.price()).isEqualByComparingTo(expectedPrice);
    }

    @Test
    @DisplayName("Should preserve quantity when mapping")
    void shouldPreserveQuantity() {
        // given
        final int expectedQuantity = 999;
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                BigDecimal.TEN,
                expectedQuantity
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.quantity()).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("Should handle minimum quantity value")
    void shouldHandleMinimumQuantity() {
        // given
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                BigDecimal.ONE,
                1
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.quantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should handle large quantity value")
    void shouldHandleLargeQuantity() {
        // given
        final int largeQuantity = Integer.MAX_VALUE;
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                BigDecimal.ONE,
                largeQuantity
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.quantity()).isEqualTo(largeQuantity);
    }

    @Test
    @DisplayName("Should handle very small price")
    void shouldHandleVerySmallPrice() {
        // given
        final BigDecimal smallPrice = new BigDecimal("0.01");
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                smallPrice,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.price()).isEqualByComparingTo(smallPrice);
    }

    @Test
    @DisplayName("Should handle very large price")
    void shouldHandleVeryLargePrice() {
        // given
        final BigDecimal largePrice = new BigDecimal("999999999.99");
        final CreateProductRequest request = new CreateProductRequest(
                UUID.randomUUID(),
                "CODE",
                "Name",
                largePrice,
                10
        );

        // when
        final CreateProductCommand command = mapper.createProductRequestToCreateProductCommand(request);

        // then
        assertThat(command.price()).isEqualByComparingTo(largePrice);
    }
}
