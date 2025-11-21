package com.kozubek.productapplication;

import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.productapplication.command.ProductCreateCommandHandler;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productapplication.query.GetProductQueryHandler;
import com.kozubek.productapplication.query.dto.ProductProjection;
import com.kozubek.ddd.annotation.domaindrivendesign.ApplicationLayer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@ApplicationLayer
@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductCreateCommandHandler productCreateCommandHandler;
    private final GetProductQueryHandler getProductQueryHandler;

    @Transactional
    public ProductId createProduct(final CreateProductCommand command) {
        return productCreateCommandHandler.createProduct(command);
    }

    public ProductProjection getProduct(final UUID productId) {
        return getProductQueryHandler.getProductById(productId);
    }
}
