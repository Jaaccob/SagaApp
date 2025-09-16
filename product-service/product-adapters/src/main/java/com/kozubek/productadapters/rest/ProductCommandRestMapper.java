package com.kozubek.productadapters.rest;

import com.kozubek.productadapters.rest.dto.CreateProductRequest;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import org.springframework.stereotype.Component;

@Component
class ProductCommandRestMapper {

    public CreateProductCommand createProductRequestToCreateProductCommand(final CreateProductRequest createProductRequest) {
        return CreateProductCommand.builder()
                .userId(createProductRequest.userId())
                .code(createProductRequest.code())
                .name(createProductRequest.name())
                .price(createProductRequest.price())
                .quantity(createProductRequest.quantity())
                .build();
    }
}
