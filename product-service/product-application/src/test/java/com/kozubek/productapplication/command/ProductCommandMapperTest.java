package com.kozubek.productapplication.command;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productdomain.core.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProductCommandMapper Tests")
class ProductCommandMapperTest {

    private ProductCommandMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductCommandMapper();
    }

    @Test
    @DisplayName("Should map CreateProductCommand to Product aggregate")
    void shouldMapCreateProductCommandToProduct() {
        // given
        final UUID userId = UUID.randomUUID();
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(userId)
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(50)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getUserId()).isEqualTo(new UserId(userId));
        assertThat(product.getCode()).isEqualTo("PROD-001");
        assertThat(product.getName()).isEqualTo("Test Product");
        assertThat(product.getPrice()).isEqualTo(new Money(BigDecimal.valueOf(99.99)));
        assertThat(product.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should map command with minimum price")
    void shouldMapCommandWithMinimumPrice() {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-002")
                .name("Cheap Product")
                .price(BigDecimal.valueOf(0.01))
                .quantity(15)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(0.01));
    }

    @Test
    @DisplayName("Should preserve all command properties in mapped product")
    void shouldPreserveAllCommandProperties() {
        // given
        final UUID userId = UUID.randomUUID();
        final String code = "SPECIAL-CODE-123";
        final String name = "Special Product with Long Name";
        final BigDecimal price = BigDecimal.valueOf(1234.56);
        final int quantity = 999;

        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(userId)
                .code(code)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getUserId().id()).isEqualTo(userId);
        assertThat(product.getCode()).isEqualTo(code);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice().amount()).isEqualByComparingTo(price);
        assertThat(product.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should map command with large quantity")
    void shouldMapCommandWithLargeQuantity() {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("BULK-001")
                .name("Bulk Product")
                .price(BigDecimal.valueOf(10.00))
                .quantity(10000)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getQuantity()).isEqualTo(10000);
    }

    @Test
    @DisplayName("Should create new Product instance for each mapping")
    void shouldCreateNewProductInstanceForEachMapping() {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-003")
                .name("Test Product")
                .price(BigDecimal.valueOf(50.00))
                .quantity(20)
                .build();

        // when
        final Product product1 = mapper.createProductCommandToProduct(command);
        final Product product2 = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product1).isNotSameAs(product2);
        assertThat(product1.getCode()).isEqualTo(product2.getCode());
    }
}
