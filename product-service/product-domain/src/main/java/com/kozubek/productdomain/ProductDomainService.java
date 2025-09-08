package com.kozubek.productdomain;

import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class ProductDomainService {

    public ProductCreatedEvent create(final Product product) {
        product.inicjalze();
        product.validate();
        log.info("Product {} has been created", product);
        return new ProductCreatedEvent(product, Instant.now());
    }
}
