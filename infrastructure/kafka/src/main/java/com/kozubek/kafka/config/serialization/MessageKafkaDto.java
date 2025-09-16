package com.kozubek.kafka.config.serialization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageKafkaDto<T extends Serializable> implements TypeDto, Serializable {

    private String dataId;
    private final String messageId =  UUID.randomUUID().toString();
    private Instant createdAt;
    private String type;
    private T data;
    private String sagaId;

    protected MessageKafkaDto(final String dataId, final Instant createdAt, final T data, final String sagaId) {
        this.dataId = dataId;
        setCreatedAt(createdAt);
        this.data = data;
        this.sagaId = sagaId;
        this.type = this.getClass().getName();
    }

    @Override
    public String getType() {
        return type;
    }

    public void setCreatedAt(Instant createdAt) {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        this.createdAt = createdAt;
    }
}
