package com.kozubek.productadapters.message.publisher;

import com.kozubek.kafka.config.producer.KafkaPublisher;
import com.kozubek.kafka.model.events.ProductCreatedEventDtoKafka;
import com.kozubek.productapplication.config.ProductServiceConfigProperties;
import com.kozubek.productapplication.message.publisher.ProductCreatedEventPublisher;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductCreatedKafkaEventPublisher implements ProductCreatedEventPublisher {
    private final ProductServiceConfigProperties properties;
    private final KafkaPublisher<ProductCreatedEventDtoKafka> kafkaPublisher;
    private final OutputMessagingKafkaDataMapper mapper;

    public void publish(final ProductCreatedEvent event) {
        final String productId = event.getProduct().getId().id().toString();

        try {
            final ProductCreatedEventDtoKafka productCreatedEventDtoKafka = mapper.productCreatedEventToProductCreatedEventDtoKafka(event);

            kafkaPublisher.send(properties.getProductCreatedTopicName(), productCreatedEventDtoKafka.getSagaId(), productCreatedEventDtoKafka);
        } catch (final Exception e) {
            log.error("Error while sending OrderCreatedEvent message to kafka. Order id: {} and SagaId: {} error: {}", productId, null, e.getMessage());
        }
    }
}
