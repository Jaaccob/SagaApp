package com.kozubek.kafka.config.producer.exception;

public class KafkaProducerException extends RuntimeException {
    public KafkaProducerException(final String message) {
        super(message);
    }
}
