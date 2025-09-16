package com.kozubek.productadapters.rest.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateProductRequest(UUID userId, String code, String name, BigDecimal price, int quantity) {
}
