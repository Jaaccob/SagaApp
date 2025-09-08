package com.kozubek.productadapters.repository;

import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.port.ProductRepository;
import com.kozubek.productentities.ProductEntity;
import com.kozubek.productentities.ProductEntityCommandMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SqlProductRepository implements ProductRepository {

    private final ProductRepositoryJpa repository;
    private final ProductEntityCommandMapper commandMapper;

    @Override
    public void save(final Product product) {
        commandMapper.productEntityToProduct(repository.save(commandMapper.productToProductEntity(product)));
    }
}

@Repository
interface ProductRepositoryJpa extends JpaRepository<ProductEntity, UUID> {
}
