package com.kozubek.productapplication.command.property;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productapplication.command.ProductCommandMapper;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productdomain.core.Product;
import net.jqwik.api.*;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.Positive;
import net.jqwik.api.constraints.StringLength;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@PropertyDefaults(tries = 100)
class ProductCommandMapperPropertyTest {

    private final ProductCommandMapper mapper = new ProductCommandMapper();

    @Property
    @Label("Mapper always preserves userId from command")
    void mapperAlwaysPreservesUserId(
            @ForAll("validUserIds") UUID userId,
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(userId)
                .code("MAP-001")
                .name("Mapper Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getUserId()).isEqualTo(new UserId(userId));
        assertThat(product.getUserId().id()).isEqualTo(userId);
    }

    @Property
    @Label("Mapper always preserves code from command")
    void mapperAlwaysPreservesCode(
            @ForAll("productCodes") String code,
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code(code)
                .name("Code Mapping Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getCode()).isEqualTo(code);
    }

    @Property
    @Label("Mapper always preserves name from command")
    void mapperAlwaysPreservesName(
            @ForAll @StringLength(min = 1, max = 200) String name,
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("NAME-MAP")
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getName()).isEqualTo(name);
    }

    @Property
    @Label("Mapper always preserves price from command with correct scaling")
    void mapperAlwaysPreservesPriceWithCorrectScaling(
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PRICE-MAP")
                .name("Price Mapping Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getPrice()).isEqualTo(new Money(price));
        assertThat(product.getPrice().amount().scale()).isEqualTo(2);
    }

    @Property
    @Label("Mapper always preserves quantity from command")
    void mapperAlwaysPreservesQuantity(
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = Integer.MAX_VALUE - 1) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("QTY-MAP")
                .name("Quantity Mapping Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getQuantity()).isEqualTo(quantity);
    }

    @Property
    @Label("Mapper creates new Product instance for each mapping")
    void mapperCreatesNewProductInstanceForEachMapping(
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("INSTANCE-MAP")
                .name("Instance Mapping Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product1 = mapper.createProductCommandToProduct(command);
        final Product product2 = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product1).isNotSameAs(product2);
        assertThat(product1.getCode()).isEqualTo(product2.getCode());
        assertThat(product1.getName()).isEqualTo(product2.getName());
    }

    @Property
    @Label("Mapper handles boundary price values correctly")
    void mapperHandlesBoundaryPriceValuesCorrectly(
            @ForAll("boundaryPrices") BigDecimal boundaryPrice,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("BOUNDARY-PRICE")
                .name("Boundary Price Mapping")
                .price(boundaryPrice)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getPrice().amount()).isEqualByComparingTo(boundaryPrice);
    }

    @Property
    @Label("Mapper handles boundary quantity values correctly")
    void mapperHandlesBoundaryQuantityValuesCorrectly(
            @ForAll @Positive BigDecimal price,
            @ForAll("boundaryQuantities") int boundaryQuantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("BOUNDARY-QTY")
                .name("Boundary Quantity Mapping")
                .price(price)
                .quantity(boundaryQuantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getQuantity()).isEqualTo(boundaryQuantity);
    }

    @Property
    @Label("Mapper preserves all properties simultaneously")
    void mapperPreservesAllPropertiesSimultaneously(
            @ForAll("validUserIds") UUID userId,
            @ForAll("productCodes") String code,
            @ForAll @StringLength(min = 1, max = 200) String name,
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
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

    @Property
    @Label("Mapped product has null ID and status before initialization")
    void mappedProductHasNullIdAndStatusBeforeInitialization(
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("NULL-CHECK")
                .name("Null Check Test")
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getId()).isNull();
        assertThat(product.getStatus()).isNull();
    }

    @Property
    @Label("Mapper handles special characters in code and name")
    void mapperHandlesSpecialCharactersInCodeAndName(
            @ForAll("specialCharacterStrings") String code,
            @ForAll("specialCharacterStrings") String name,
            @ForAll @Positive BigDecimal price,
            @ForAll @IntRange(min = 1, max = 10000) int quantity
    ) {
        // given
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code(code)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();

        // when
        final Product product = mapper.createProductCommandToProduct(command);

        // then
        assertThat(product.getCode()).isEqualTo(code);
        assertThat(product.getName()).isEqualTo(name);
    }

    // Providers
    @Provide
    Arbitrary<UUID> validUserIds() {
        return Arbitraries.create(() -> UUID.randomUUID());
    }

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
    Arbitrary<BigDecimal> boundaryPrices() {
        return Arbitraries.of(
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(0.99),
                BigDecimal.valueOf(1.00),
                BigDecimal.valueOf(9.99),
                BigDecimal.valueOf(99.99),
                BigDecimal.valueOf(999.99),
                BigDecimal.valueOf(9999.99)
        );
    }

    @Provide
    Arbitrary<Integer> boundaryQuantities() {
        return Arbitraries.of(1, 2, 10, 11, 50, 100, 999, 1000, 9999, 10000);
    }

    @Provide
    Arbitrary<String> specialCharacterStrings() {
        return Arbitraries.strings()
                .alpha()
                .numeric()
                .withChars(' ', '-', '_', '.', '&', '(', ')', '/', '\\')
                .ofMinLength(1)
                .ofMaxLength(50);
    }
}
