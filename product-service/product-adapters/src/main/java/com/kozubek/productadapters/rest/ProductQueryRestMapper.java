package com.kozubek.productadapters.rest;

import com.kozubek.productadapters.rest.dto.GetDetailsProductResponse;
import com.kozubek.productapplication.query.dto.ProductProjection;
import org.springframework.stereotype.Component;

@Component
class ProductQueryRestMapper {

    public GetDetailsProductResponse productToGetDetailsProductResponse(final ProductProjection product) {
        return GetDetailsProductResponse.builder()
                .productId(product.productId())
                .userId(product.userId())
                .code(product.code())
                .name(product.name())
                .price(product.price())
                .quantity(product.quantity())
                .status(product.status())
                .build();
    }
}
