package com.kozubek.productapplication.query;

import com.kozubek.productapplication.query.dto.ProductProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetProductQueryHandler {

    private final ProductQueryRepository productQueryRepository;

    @Transactional(readOnly = true)
    public ProductProjection getProductById(final UUID productId) {
        return productQueryRepository.getProductProjection(productId);
    }
}
