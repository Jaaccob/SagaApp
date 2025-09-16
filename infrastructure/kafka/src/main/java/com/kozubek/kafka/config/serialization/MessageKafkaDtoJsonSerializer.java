package com.kozubek.kafka.config.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageKafkaDtoJsonSerializer<T extends MessageKafkaDto<T>> extends JsonSerializer<T> {

    @Override
    public byte[] serialize(final String topic, final T data) {
        try {
            if (data == null) {
                log.error("Empty message to serialize");
                return new byte[0];
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (final JsonProcessingException e) {
            throw new SerializationException("Error serializing message to JSON", e);
        }
    }
}
