package com.kozubek.productapplication.query.dto;

import com.kozubek.commondomain.vo.ProductStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ProductProjection(
        UUID productId,
        UUID userId,
        ProductStatus status,
        String code,
        String name,
        BigDecimal price,
        Integer quantity
) {
}
