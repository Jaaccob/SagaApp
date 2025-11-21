package com.kozubek.productdomain.event;

import com.kozubek.ddd.annotation.domaindrivendesign.DomainEvent;
import com.kozubek.productdomain.core.Product;

import java.time.Instant;

@DomainEvent
public class ProductCreatedEvent extends ProductEvent {
	public ProductCreatedEvent(final Product product, final Instant createdAt) {
		super(product, createdAt);
	}
}
