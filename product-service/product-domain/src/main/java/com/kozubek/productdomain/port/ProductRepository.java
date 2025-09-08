package com.kozubek.productdomain.port;

import com.kozubek.productdomain.core.Product;

public interface ProductRepository {

    void save(Product product);
}
