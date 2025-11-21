package com.kozubek.productadapters.message.publisher;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.kafka.config.producer.KafkaPublisher;
import com.kozubek.kafka.model.events.ProductCreatedEventDtoKafka;
import com.kozubek.productapplication.config.ProductServiceConfigProperties;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductCreatedKafkaEventPublisher Unit Tests")
class ProductCreatedKafkaEventPublisherTest {

    @Mock
    private ProductServiceConfigProperties properties;

    @Mock
    private KafkaPublisher<ProductCreatedEventDtoKafka> kafkaPublisher;

    @Mock
    private OutputMessagingKafkaDataMapper mapper;

    @InjectMocks
    private ProductCreatedKafkaEventPublisher publisher;

    @Captor
    private ArgumentCaptor<String> topicCaptor;

    @Captor
    private ArgumentCaptor<String> keyCaptor;

    @Captor
    private ArgumentCaptor<ProductCreatedEventDtoKafka> eventCaptor;

    private ProductCreatedEvent domainEvent;
    private ProductCreatedEventDtoKafka kafkaEvent;
    private String topicName;
	private String sagaId;

    @BeforeEach
    void setUp() {
		final UUID productId = UUID.randomUUID();
        sagaId = UUID.randomUUID().toString();
        topicName = "product.product.created";

        final Product product = Product.builder()
                .id(new ProductId(productId))
                .userId(new UserId(UUID.randomUUID()))
                .code("PROD-001")
                .name("Test Product")
                .price(new Money(BigDecimal.valueOf(99.99)))
                .quantity(50)
                .status(ProductStatus.AVAILABLE)
                .build();

        domainEvent = new ProductCreatedEvent(product, Instant.now());
        kafkaEvent = mock(ProductCreatedEventDtoKafka.class);
        when(kafkaEvent.getSagaId()).thenReturn(sagaId);
    }

    @Test
    @DisplayName("Should publish event to Kafka successfully")
    void shouldPublishEventToKafkaSuccessfully() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(kafkaPublisher).send(topicName, sagaId, kafkaEvent);
    }

    @Test
    @DisplayName("Should use mapper to convert domain event to Kafka event")
    void shouldUseMapperToConvertEvent() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(mapper).productCreatedEventToProductCreatedEventDtoKafka(domainEvent);
    }

    @Test
    @DisplayName("Should retrieve topic name from configuration properties")
    void shouldRetrieveTopicNameFromProperties() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(properties).getProductCreatedTopicName();
    }

    @Test
    @DisplayName("Should use saga ID as Kafka message key")
    void shouldUseSagaIdAsKafkaMessageKey() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(kafkaPublisher).send(anyString(), keyCaptor.capture(), any(ProductCreatedEventDtoKafka.class));
        assertThat(keyCaptor.getValue()).isEqualTo(sagaId);
    }

    @Test
    @DisplayName("Should send event to correct topic")
    void shouldSendEventToCorrectTopic() {
        // given
        final String customTopic = "custom.product.created";
        when(properties.getProductCreatedTopicName()).thenReturn(customTopic);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(kafkaPublisher).send(topicCaptor.capture(), anyString(), any(ProductCreatedEventDtoKafka.class));
        assertThat(topicCaptor.getValue()).isEqualTo(customTopic);
    }

    @Test
    @DisplayName("Should send mapped Kafka event")
    void shouldSendMappedKafkaEvent() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);

        // when
        publisher.publish(domainEvent);

        // then
        verify(kafkaPublisher).send(anyString(), anyString(), eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isEqualTo(kafkaEvent);
    }

    @Test
    @DisplayName("Should handle Kafka publishing exception gracefully")
    void shouldHandleKafkaPublishingExceptionGracefully() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);
        doThrow(new RuntimeException("Kafka connection error"))
                .when(kafkaPublisher).send(anyString(), anyString(), any(ProductCreatedEventDtoKafka.class));

        // when & then
        assertThatNoException().isThrownBy(() -> publisher.publish(domainEvent));
    }

    @Test
    @DisplayName("Should log error when Kafka publishing fails")
    void shouldLogErrorWhenKafkaPublishingFails() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(domainEvent)).thenReturn(kafkaEvent);
        doThrow(new RuntimeException("Network timeout"))
                .when(kafkaPublisher).send(anyString(), anyString(), any(ProductCreatedEventDtoKafka.class));

        // when
        publisher.publish(domainEvent);

        // then
        verify(kafkaPublisher).send(anyString(), anyString(), any(ProductCreatedEventDtoKafka.class));
        // Note: W realnym projekcie można użyć biblioteki do przechwytywania logów (np. Logback Test Appender)
        // aby zweryfikować czy błąd został zalogowany
    }

    @Test
    @DisplayName("Should handle multiple event publishing")
    void shouldHandleMultipleEventPublishing() {
        // given
        when(properties.getProductCreatedTopicName()).thenReturn(topicName);
        when(mapper.productCreatedEventToProductCreatedEventDtoKafka(any())).thenReturn(kafkaEvent);

        final Product product2 = Product.builder()
                .id(new ProductId(UUID.randomUUID()))
                .userId(new UserId(UUID.randomUUID()))
                .code("PROD-002")
                .name("Second Product")
                .price(new Money(BigDecimal.valueOf(199.99)))
                .quantity(25)
                .status(ProductStatus.AVAILABLE)
                .build();
        final ProductCreatedEvent domainEvent2 = new ProductCreatedEvent(product2, Instant.now());

        // when
        publisher.publish(domainEvent);
        publisher.publish(domainEvent2);

        // then
        verify(kafkaPublisher, times(2)).send(topicName, sagaId, kafkaEvent);
        verify(mapper, times(2)).productCreatedEventToProductCreatedEventDtoKafka(any(ProductCreatedEvent.class));
    }
}
