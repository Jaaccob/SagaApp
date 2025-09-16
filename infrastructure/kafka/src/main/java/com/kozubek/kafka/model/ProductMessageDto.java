package com.kozubek.kafka.model;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductMessageDto(String productId, String userId, String code, String name, BigDecimal price,
                                int quantity, String status) implements Serializable {
}
