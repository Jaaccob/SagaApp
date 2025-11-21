package com.kozubek.productapplication.query;

import com.kozubek.productapplication.query.dto.ProductProjection;
import com.kozubek.ddd.annotation.architecture.cqrs.QueryOperation;
import com.kozubek.ddd.annotation.architecture.portsandadapters.DrivingPort;
import com.kozubek.ddd.annotation.domaindrivendesign.ApplicationLayer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@ApplicationLayer
@Slf4j
@Component
@RequiredArgsConstructor
@QueryOperation
@DrivingPort
public class GetProductQueryHandler {

    private final ProductQueryRepository productQueryRepository;

    @Transactional(readOnly = true)
    public ProductProjection getProductById(final UUID productId) {
        return productQueryRepository.getProductProjection(productId);
    }
}
