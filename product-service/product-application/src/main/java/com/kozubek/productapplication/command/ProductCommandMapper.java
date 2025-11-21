package com.kozubek.productapplication.command;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productdomain.core.Product;

public class ProductCommandMapper {
    public Product createProductCommandToProduct(final CreateProductCommand command) {
        return Product.builder()
                .userId(new UserId(command.userId()))
                .code(command.code())
                .name(command.name())
                .price(new Money(command.price()))
                .quantity(command.quantity())
                .build();
    }
}
