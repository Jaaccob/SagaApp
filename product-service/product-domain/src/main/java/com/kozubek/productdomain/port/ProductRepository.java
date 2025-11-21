package com.kozubek.productdomain.port;

import com.kozubek.ddd.annotation.domaindrivendesign.DomainRepository;
import com.kozubek.productdomain.core.Product;

@DomainRepository
public interface ProductRepository {
    void save(Product product);
}
