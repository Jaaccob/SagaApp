package com.kozubek.productadapters.message.publisher;

import com.kozubek.kafka.model.ProductMessageDto;
import com.kozubek.kafka.model.events.ProductCreatedEventDtoKafka;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class OutputMessagingKafkaDataMapper {

    public ProductCreatedEventDtoKafka productCreatedEventToProductCreatedEventDtoKafka(final ProductCreatedEvent productCreatedEvent) {
        final Product messagePayload = productCreatedEvent.getProduct();

        final ProductMessageDto productMessageDto = ProductMessageDto.builder()
                .productId(messagePayload.getId().id().toString())
                .userId(messagePayload.getUserId().id().toString())
                .code(messagePayload.getCode())
                .name(messagePayload.getName())
                .price(messagePayload.getPrice().amount())
                .quantity(messagePayload.getQuantity())
                .status(messagePayload.getStatus().toString())
                .build();

        return new ProductCreatedEventDtoKafka(productMessageDto, productMessageDto.productId(), productCreatedEvent.getCreatedAt(), UUID.randomUUID().toString());
    }
}
