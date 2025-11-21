package com.kozubek.productdomain;

import com.kozubek.ddd.annotation.domaindrivendesign.DomainService;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;

import java.time.Instant;

@DomainService
public class ProductDomainService {
	public ProductCreatedEvent create(final Product product) {
		product.initialize();
		product.validate();
		return new ProductCreatedEvent(product, Instant.now());
	}
}
