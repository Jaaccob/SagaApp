package com.kozubek.productapplication.command;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productapplication.message.publisher.ProductCreatedEventPublisher;
import com.kozubek.productdomain.ProductDomainService;
import com.kozubek.productdomain.core.Product;
import com.kozubek.productdomain.event.ProductCreatedEvent;
import com.kozubek.productdomain.exception.ProductDomainException;
import com.kozubek.productdomain.port.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductCreateCommandHandler Tests")
class ProductCreateCommandHandlerTest {

    @Mock
    private ProductDomainService productDomainService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCreatedEventPublisher publisher;

    @InjectMocks
    private ProductCreateCommandHandler commandHandler;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @Captor
    private ArgumentCaptor<ProductCreatedEvent> eventCaptor;

    private CreateProductCommand validCommand;
    private ProductId expectedProductId;

    @BeforeEach
    void setUp() {
        expectedProductId = new ProductId(UUID.randomUUID());
        validCommand = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .quantity(20)
                .build();
    }

    @Test
    @DisplayName("Should create product successfully following application layer pattern")
    void shouldCreateProductSuccessfully() {
        // given
        final Product product = Product.builder()
                .id(expectedProductId)
                .userId(new UserId(validCommand.userId()))
                .code(validCommand.code())
                .name(validCommand.name())
                .price(new Money(validCommand.price()))
                .quantity(validCommand.quantity())
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());

        when(productDomainService.create(any(Product.class))).thenReturn(event);

        // when
        final ProductId result = commandHandler.createProduct(validCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedProductId);

        // Verify 1 domain operation
        verify(productDomainService, times(1)).create(productCaptor.capture());
        final Product capturedProduct = productCaptor.getValue();
        assertThat(capturedProduct.getCode()).isEqualTo("PROD-001");
        assertThat(capturedProduct.getName()).isEqualTo("Test Product");
        assertThat(capturedProduct.getQuantity()).isEqualTo(20);

        // Verify output operations
        verify(productRepository, times(1)).save(capturedProduct);
        verify(publisher, times(1)).publish(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isEqualTo(event);
    }

    @Test
    @DisplayName("Should execute operations in correct order: domain -> save -> publish")
    void shouldExecuteOperationsInCorrectOrder() {
        // given
        final Product product = Product.builder()
                .id(expectedProductId)
                .userId(new UserId(validCommand.userId()))
                .code(validCommand.code())
                .name(validCommand.name())
                .price(new Money(validCommand.price()))
                .quantity(validCommand.quantity())
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());
        when(productDomainService.create(any(Product.class))).thenReturn(event);

        // when
        commandHandler.createProduct(validCommand);

        // then
        final var inOrder = inOrder(productDomainService, productRepository, publisher);
        inOrder.verify(productDomainService).create(any(Product.class));
        inOrder.verify(productRepository).save(any(Product.class));
        inOrder.verify(publisher).publish(any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Should throw exception when domain validation fails")
    void shouldThrowExceptionWhenDomainValidationFails() {
        // given
        final CreateProductCommand invalidCommand = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("INVALID")
                .name("Invalid Product")
                .price(BigDecimal.ZERO)
                .quantity(20)
                .build();

        when(productDomainService.create(any(Product.class)))
                .thenThrow(new ProductDomainException("Product price: 0 must be greater than zero"));

        // when & then
        assertThatThrownBy(() -> commandHandler.createProduct(invalidCommand))
                .isInstanceOf(ProductDomainException.class)
                .hasMessageContaining("price")
                .hasMessageContaining("must be greater than zero");

        // Verify no output operations were executed
        verify(productRepository, never()).save(any(Product.class));
        verify(publisher, never()).publish(any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Should not publish event if repository save fails")
    void shouldNotPublishEventIfRepositorySaveFails() {
        // given
        final Product product = Product.builder()
                .id(expectedProductId)
                .userId(new UserId(validCommand.userId()))
                .code(validCommand.code())
                .name(validCommand.name())
                .price(new Money(validCommand.price()))
                .quantity(validCommand.quantity())
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());
        when(productDomainService.create(any(Product.class))).thenReturn(event);
        doThrow(new RuntimeException("Database error")).when(productRepository).save(any(Product.class));

        // when & then
        assertThatThrownBy(() -> commandHandler.createProduct(validCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Database error");

        verify(productDomainService, times(1)).create(any(Product.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(publisher, never()).publish(any(ProductCreatedEvent.class));
    }

    @Test
    @DisplayName("Should map command correctly before passing to domain service")
    void shouldMapCommandCorrectly() {
        // given
        final UUID userId = UUID.randomUUID();
        final CreateProductCommand command = CreateProductCommand.builder()
                .userId(userId)
                .code("MAPPED-001")
                .name("Mapped Product")
                .price(BigDecimal.valueOf(49.99))
                .quantity(15)
                .build();

        final Product mappedProduct = Product.builder()
                .id(expectedProductId)
                .userId(new UserId(userId))
                .code("MAPPED-001")
                .name("Mapped Product")
                .price(new Money(BigDecimal.valueOf(49.99)))
                .quantity(15)
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(mappedProduct, Instant.now());
        when(productDomainService.create(any(Product.class))).thenReturn(event);

        // when
        commandHandler.createProduct(command);

        // then
        verify(productDomainService).create(productCaptor.capture());
        final Product captured = productCaptor.getValue();
        assertThat(captured.getUserId().id()).isEqualTo(userId);
        assertThat(captured.getCode()).isEqualTo("MAPPED-001");
        assertThat(captured.getName()).isEqualTo("Mapped Product");
        assertThat(captured.getPrice().amount()).isEqualByComparingTo(BigDecimal.valueOf(49.99));
        assertThat(captured.getQuantity()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should return ProductId from created event")
    void shouldReturnProductIdFromCreatedEvent() {
        // given
        final ProductId expectedId = new ProductId(UUID.randomUUID());
        final Product product = Product.builder()
                .id(expectedId)
                .userId(new UserId(validCommand.userId()))
                .code(validCommand.code())
                .name(validCommand.name())
                .price(new Money(validCommand.price()))
                .quantity(validCommand.quantity())
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());
        when(productDomainService.create(any(Product.class))).thenReturn(event);

        // when
        final ProductId result = commandHandler.createProduct(validCommand);

        // then
        assertThat(result).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("Should handle boundary values correctly")
    void shouldHandleBoundaryValuesCorrectly() {
        // given
        final CreateProductCommand boundaryCommand = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("BOUNDARY-001")
                .name("Boundary Product")
                .price(BigDecimal.valueOf(0.01))
                .quantity(11)
                .build();

        final Product product = Product.builder()
                .id(expectedProductId)
                .userId(new UserId(boundaryCommand.userId()))
                .code(boundaryCommand.code())
                .name(boundaryCommand.name())
                .price(new Money(boundaryCommand.price()))
                .quantity(boundaryCommand.quantity())
                .build();

        final ProductCreatedEvent event = new ProductCreatedEvent(product, Instant.now());
        when(productDomainService.create(any(Product.class))).thenReturn(event);

        // when
        final ProductId result = commandHandler.createProduct(boundaryCommand);

        // then
        assertThat(result).isNotNull();
        verify(productDomainService).create(any(Product.class));
        verify(productRepository).save(any(Product.class));
        verify(publisher).publish(any(ProductCreatedEvent.class));
    }
}
