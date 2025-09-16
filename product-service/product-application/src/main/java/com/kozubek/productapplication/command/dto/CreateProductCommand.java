package com.kozubek.productapplication.command.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CreateProductCommand(
        UUID userId,
        String code,
        String name,
        BigDecimal price,
        int quantity
) {
}
