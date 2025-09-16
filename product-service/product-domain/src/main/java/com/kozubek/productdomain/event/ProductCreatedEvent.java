package com.kozubek.productdomain.event;

import com.kozubek.productdomain.core.Product;

import java.time.Instant;

public class ProductCreatedEvent extends ProductEvent {

    public ProductCreatedEvent(final Product product, final Instant createdAt) {
        super(product, createdAt);
    }
}
