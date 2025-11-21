package com.kozubek.productadapters.repository;

import com.kozubek.ddd.annotation.architecture.portsandadapters.DrivenAdapter;
import com.kozubek.productapplication.exception.ProductNotFoundException;
import com.kozubek.productapplication.query.ProductQueryRepository;
import com.kozubek.productapplication.query.dto.ProductProjection;
import com.kozubek.productentities.ProductEntity;
import com.kozubek.productentities.ProductEntityQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@DrivenAdapter
@Repository
@RequiredArgsConstructor
public class SqlProductQueryRepository implements ProductQueryRepository {
	private final ProductQueryRepositoryJpa repository;
	private final ProductEntityQueryMapper mapper = new ProductEntityQueryMapper();

	@Override
	public ProductProjection getProductProjection(final UUID productId) {
		return repository.findById(productId)
				.map(mapper::productToProductProjection)
				.orElseThrow(() -> new ProductNotFoundException("Could not find product with id: " + productId));
	}
}

@Repository
interface ProductQueryRepositoryJpa extends JpaRepository<ProductEntity, UUID> {
}
