package com.kozubek.productapplication.command;

import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productdomain.ProductDomainService;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import com.kozubek.productdomain.port.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductCreateCommandHandler {

    private final ProductCommandMapper productCommandMapper;
    private final ProductDomainService productDomainService;
    private final ProductRepository productRepository;

    @Transactional
    public ProductId createProduct(final CreateProductCommand command) {
        final Product product = productCommandMapper.createProductCommandToProduct(command);

        final ProductCreatedEvent productCreatedEvent = productDomainService.create(product);
        final ProductId productId = productCreatedEvent.getProduct().getId();

        productRepository.save(product);
        //save to outbox database

        log.info("Product created with id {}",  productId);
        return productId;
    }
}
