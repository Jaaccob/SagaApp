package com.kozubek.productapplication.command;

import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.ddd.annotation.architecture.cqrs.CommandOperation;
import com.kozubek.ddd.annotation.architecture.portsandadapters.DrivingPort;
import com.kozubek.ddd.annotation.domaindrivendesign.ApplicationLayer;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productapplication.message.publisher.ProductCreatedEventPublisher;
import com.kozubek.productdomain.ProductDomainService;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import com.kozubek.productdomain.port.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@ApplicationLayer
@Component
@Slf4j
@RequiredArgsConstructor
@CommandOperation
@DrivingPort
public class ProductCreateCommandHandler {
	private final ProductDomainService productDomainService;
	private final ProductRepository productRepository;
	private final ProductCreatedEventPublisher publisher;
	private final ProductCommandMapper productCommandMapper = new ProductCommandMapper();

	@Transactional
	public ProductId createProduct(final CreateProductCommand command) {
		final Product product = productCommandMapper.createProductCommandToProduct(command);

		final ProductCreatedEvent productCreatedEvent = productDomainService.create(product);
		final ProductId productId = productCreatedEvent.getProduct().getId();

		productRepository.save(product);
		publisher.publish(productCreatedEvent); //TODO jk - change to outbox pattern
		//save to outbox database

		log.info("Product created with id {}", productId);
		return productId;
	}
}
