package com.kozubek.inventoryadapters.message.consumer;

import com.kozubek.kafka.config.consumer.KafkaConsumer;
import com.kozubek.kafka.model.events.ProductCreatedEventDtoKafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductCreatedKafkaEventConsumer implements KafkaConsumer<ProductCreatedEventDtoKafka> {

    @Override
    @KafkaListener(topics = "${payment-service.order-created-events-topic-name}")
    public void receive(List<ProductCreatedEventDtoKafka> messages, List<String> keys, List<Integer> partitions, List<Long> offsets) {

    }
}
