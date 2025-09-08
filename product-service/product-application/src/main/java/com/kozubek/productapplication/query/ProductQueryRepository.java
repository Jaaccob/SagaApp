package com.kozubek.productapplication.query;

import com.kozubek.productapplication.query.dto.ProductProjection;

import java.util.UUID;

public interface ProductQueryRepository {
    ProductProjection getProductProjection(UUID productId);
}
