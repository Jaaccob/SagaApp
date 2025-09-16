package com.kozubek.productentities;

import com.kozubek.productapplication.query.dto.ProductProjection;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityQueryMapper {

    public ProductProjection productToProductProjection(final ProductEntity product) {
        return ProductProjection.builder()
                .productId(product.getId())
                .userId(product.getUserId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .status(product.getStatus())
                .build();
    }
}
