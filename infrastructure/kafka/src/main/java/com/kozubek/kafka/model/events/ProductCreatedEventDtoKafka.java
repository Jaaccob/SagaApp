package com.kozubek.kafka.model.events;

import com.kozubek.kafka.config.serialization.MessageKafkaDto;
import com.kozubek.kafka.model.ProductMessageDto;

import java.time.Instant;

public class ProductCreatedEventDtoKafka extends MessageKafkaDto<ProductMessageDto> {

    public ProductCreatedEventDtoKafka(final ProductMessageDto productMessageDto, final String itemId, final Instant createdAt, final String sagaId) {
        super(itemId, createdAt, productMessageDto, sagaId);
    }
}
