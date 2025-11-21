package com.kozubek.productadapters.message.publisher;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.kafka.model.ProductMessageDto;
import com.kozubek.kafka.model.events.ProductCreatedEventDtoKafka;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OutputMessagingKafkaDataMapper Unit Tests")
class OutputMessagingKafkaDataMapperTest {

    private OutputMessagingKafkaDataMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OutputMessagingKafkaDataMapper();
    }

    @Test
    @DisplayName("Should map ProductCreatedEvent to ProductCreatedEventDtoKafka with all fields preserved")
    void shouldMapProductCreatedEventToKafkaDto() {
        // given
        final UUID productId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();
        final Instant createdAt = Instant.now();

        final Product product = Product.builder()
                .id(new ProductId(productId))
                .userId(new UserId(userId))
                .code("PROD-001")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(99.99)))
                .quantity(50)
                .status(ProductStatus.AVAILABLE)
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, createdAt);

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto).isNotNull();
        assertThat(kafkaDto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(kafkaDto.getSagaId()).isNotNull();
        
        final ProductMessageDto messageDto = kafkaDto.getData();
        assertThat(messageDto).isNotNull();
        assertThat(messageDto.productId()).isEqualTo(productId.toString());
        assertThat(messageDto.userId()).isEqualTo(userId.toString());
        assertThat(messageDto.code()).isEqualTo("PROD-001");
        assertThat(messageDto.name()).isEqualTo("Test Product");
        assertThat(messageDto.price()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
        assertThat(messageDto.quantity()).isEqualTo(50);
        assertThat(messageDto.status()).isEqualTo("AVAILABLE");
    }

    @Test
    @DisplayName("Should preserve product ID when mapping")
    void shouldPreserveProductId() {
        // given
        final UUID expectedProductId = UUID.randomUUID();
        final Product product = Product.builder()
                .id(new ProductId(expectedProductId))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().productId()).isEqualTo(expectedProductId.toString());
        assertThat(kafkaDto.getDataId()).isEqualTo(expectedProductId.toString());
    }

    @Test
    @DisplayName("Should preserve user ID when mapping")
    void shouldPreserveUserId() {
        // given
        final UUID expectedUserId = UUID.randomUUID();
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(expectedUserId))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().userId()).isEqualTo(expectedUserId.toString());
    }

    @Test
    @DisplayName("Should preserve code when mapping")
    void shouldPreserveCode() {
        // given
        final String expectedCode = "SPECIAL-CODE-123";
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code(expectedCode)
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Should preserve name when mapping")
    void shouldPreserveName() {
        // given
        final String expectedName = "Product with Special Characters !@# 123";
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name(expectedName)
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().name()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("Should preserve price when mapping")
    void shouldPreservePrice() {
        // given
        final BigDecimal expectedPrice = new BigDecimal("1234.56");
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(expectedPrice))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().price()).isEqualByComparingTo(expectedPrice);
    }

    @Test
    @DisplayName("Should preserve quantity when mapping")
    void shouldPreserveQuantity() {
        // given
        final int expectedQuantity = 999;
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(expectedQuantity)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().quantity()).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("Should preserve AVAILABLE status when mapping")
    void shouldPreserveAvailableStatus() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(20)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().status()).isEqualTo("AVAILABLE");
    }

    @Test
    @DisplayName("Should preserve NOT_AVAILABLE status when mapping")
    void shouldPreserveNotAvailableStatus() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(5)
                .status(ProductStatus.NOT_AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().status()).isEqualTo("NOT_AVAILABLE");
    }

    @Test
    @DisplayName("Should preserve createdAt timestamp when mapping")
    void shouldPreserveCreatedAtTimestamp() {
        // given
        final Instant expectedTimestamp = Instant.parse("2025-11-01T12:00:00Z");
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, expectedTimestamp);

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getCreatedAt()).isEqualTo(expectedTimestamp);
    }

    @Test
    @DisplayName("Should generate unique saga ID for each mapping")
    void shouldGenerateUniqueSagaIdForEachMapping() {
        // given
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto1 = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);
        final ProductCreatedEventDtoKafka kafkaDto2 = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto1.getSagaId()).isNotNull();
        assertThat(kafkaDto2.getSagaId()).isNotNull();
        assertThat(kafkaDto1.getSagaId()).isNotEqualTo(kafkaDto2.getSagaId());
    }

    @Test
    @DisplayName("Should set product ID as Kafka event ID")
    void shouldSetProductIdAsKafkaEventId() {
        // given
        final UUID productId = UUID.randomUUID();
        final Product product = Product.builder()
                .id(new ProductId(productId))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(BigDecimal.TEN))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getDataId()).isEqualTo(productId.toString());
    }

    @Test
    @DisplayName("Should handle very small price values")
    void shouldHandleVerySmallPriceValues() {
        // given
        final BigDecimal smallPrice = new BigDecimal("0.01");
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(smallPrice))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().price()).isEqualByComparingTo(smallPrice);
    }

    @Test
    @DisplayName("Should handle very large price values")
    void shouldHandleVeryLargePriceValues() {
        // given
        final BigDecimal largePrice = new BigDecimal("999999999.99");
        final Product product = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("CODE")
                .name("Name")
                .price(new Money(largePrice))
                .quantity(10)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        // when
        final ProductCreatedEventDtoKafka kafkaDto = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

        // then
        assertThat(kafkaDto.getData().price()).isEqualByComparingTo(largePrice);
    }
}
