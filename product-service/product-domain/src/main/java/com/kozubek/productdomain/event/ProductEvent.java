package com.kozubek.productdomain.event;

import com.kozubek.commondomain.event.DomainEvent;
import com.kozubek.productdomain.core.Product;
import lombok.Getter;

import java.time.Instant;


@Getter
public abstract class ProductEvent implements DomainEvent<Product> {

    private final Product product;
    private final Instant createdAt;

    ProductEvent(final Product product, final Instant createdAt) {
        this.product = product;
        this.createdAt = createdAt;
    }
}
