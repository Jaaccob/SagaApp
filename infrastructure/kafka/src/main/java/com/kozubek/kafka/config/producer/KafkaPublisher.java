package com.kozubek.kafka.config.producer;

import com.kozubek.kafka.config.producer.exception.KafkaProducerException;
import com.kozubek.kafka.config.serialization.MessageKafkaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaPublisher<V extends MessageKafkaDto<?>> {

    private final KafkaTemplate<String, V> kafkaTemplate;

    public void send(final String topicName, final String key, final V message) {
        log.info("Sending message: {} to topic: {}", message, topicName);
        try {
            kafkaTemplate.send(topicName, key, message);
        } catch (final Exception e) {
            log.error("Error sending message: {} to topic: {}", message, topicName, e);
            throw new KafkaProducerException(e.getMessage());
        }
    }
}
