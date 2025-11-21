package com.kozubek.productapplication;

import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.productapplication.command.ProductCreateCommandHandler;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productapplication.query.GetProductQueryHandler;
import com.kozubek.productapplication.query.dto.ProductProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductApplicationService Tests")
class ProductApplicationServiceTest {

    @Mock
    private ProductCreateCommandHandler productCreateCommandHandler;

    @Mock
    private GetProductQueryHandler getProductQueryHandler;

    @InjectMocks
    private ProductApplicationService applicationService;

    private CreateProductCommand createCommand;
    private UUID productId;
    private ProductId expectedProductId;
    private ProductProjection productProjection;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        expectedProductId = new ProductId(productId);

        createCommand = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .quantity(20)
                .build();

        productProjection = ProductProjection.builder()
                .productId(productId)
                .userId(UUID.randomUUID())
                .status(ProductStatus.AVAILABLE)
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(100.00))
                .quantity(20)
                .build();
    }

    @Test
    @DisplayName("Should create product by delegating to command handler")
    void shouldCreateProductByDelegatingToCommandHandler() {
        // given
        when(productCreateCommandHandler.createProduct(createCommand)).thenReturn(expectedProductId);

        // when
        final ProductId result = applicationService.createProduct(createCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedProductId);
        verify(productCreateCommandHandler, times(1)).createProduct(createCommand);
        verifyNoInteractions(getProductQueryHandler);
    }

    @Test
    @DisplayName("Should get product by delegating to query handler")
    void shouldGetProductByDelegatingToQueryHandler() {
        // given
        when(getProductQueryHandler.getProductById(productId)).thenReturn(productProjection);

        // when
        final ProductProjection result = applicationService.getProduct(productId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(productProjection);
        assertThat(result.productId()).isEqualTo(productId);
        verify(getProductQueryHandler, times(1)).getProductById(productId);
        verifyNoInteractions(productCreateCommandHandler);
    }

    @Test
    @DisplayName("Should propagate exception from command handler")
    void shouldPropagateExceptionFromCommandHandler() {
        // given
        when(productCreateCommandHandler.createProduct(any(CreateProductCommand.class)))
                .thenThrow(new RuntimeException("Command handler error"));

        // when & then
        assertThatThrownBy(() -> applicationService.createProduct(createCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Command handler error");

        verify(productCreateCommandHandler, times(1)).createProduct(createCommand);
    }

    @Test
    @DisplayName("Should propagate exception from query handler")
    void shouldPropagateExceptionFromQueryHandler() {
        // given
        when(getProductQueryHandler.getProductById(any(UUID.class)))
                .thenThrow(new RuntimeException("Query handler error"));

        // when & then
        assertThatThrownBy(() -> applicationService.getProduct(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Query handler error");

        verify(getProductQueryHandler, times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("Should handle multiple create operations")
    void shouldHandleMultipleCreateOperations() {
        // given
        final CreateProductCommand command1 = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-001")
                .name("Product 1")
                .price(BigDecimal.valueOf(100.00))
                .quantity(20)
                .build();

        final CreateProductCommand command2 = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("PROD-002")
                .name("Product 2")
                .price(BigDecimal.valueOf(200.00))
                .quantity(30)
                .build();

        final ProductId productId1 = new ProductId(UUID.randomUUID());
        final ProductId productId2 = new ProductId(UUID.randomUUID());

        when(productCreateCommandHandler.createProduct(command1)).thenReturn(productId1);
        when(productCreateCommandHandler.createProduct(command2)).thenReturn(productId2);

        // when
        final ProductId result1 = applicationService.createProduct(command1);
        final ProductId result2 = applicationService.createProduct(command2);

        // then
        assertThat(result1).isEqualTo(productId1);
        assertThat(result2).isEqualTo(productId2);
        verify(productCreateCommandHandler, times(1)).createProduct(command1);
        verify(productCreateCommandHandler, times(1)).createProduct(command2);
    }

    @Test
    @DisplayName("Should handle multiple query operations")
    void shouldHandleMultipleQueryOperations() {
        // given
        final UUID productId1 = UUID.randomUUID();
        final UUID productId2 = UUID.randomUUID();

        final ProductProjection projection1 = ProductProjection.builder()
                .productId(productId1)
                .userId(UUID.randomUUID())
                .status(ProductStatus.AVAILABLE)
                .code("PROD-001")
                .name("Product 1")
                .price(BigDecimal.valueOf(100.00))
                .quantity(20)
                .build();

        final ProductProjection projection2 = ProductProjection.builder()
                .productId(productId2)
                .userId(UUID.randomUUID())
                .status(ProductStatus.AVAILABLE)
                .code("PROD-002")
                .name("Product 2")
                .price(BigDecimal.valueOf(200.00))
                .quantity(30)
                .build();

        when(getProductQueryHandler.getProductById(productId1)).thenReturn(projection1);
        when(getProductQueryHandler.getProductById(productId2)).thenReturn(projection2);

        // when
        final ProductProjection result1 = applicationService.getProduct(productId1);
        final ProductProjection result2 = applicationService.getProduct(productId2);

        // then
        assertThat(result1).isEqualTo(projection1);
        assertThat(result2).isEqualTo(projection2);
        verify(getProductQueryHandler, times(1)).getProductById(productId1);
        verify(getProductQueryHandler, times(1)).getProductById(productId2);
    }

    @Test
    @DisplayName("Should create and then query the same product")
    void shouldCreateAndThenQueryTheSameProduct() {
        // given
        when(productCreateCommandHandler.createProduct(createCommand)).thenReturn(expectedProductId);
        when(getProductQueryHandler.getProductById(productId)).thenReturn(productProjection);

        // when
        final ProductId createdId = applicationService.createProduct(createCommand);
        final ProductProjection queriedProduct = applicationService.getProduct(productId);

        // then
        assertThat(createdId).isEqualTo(expectedProductId);
        assertThat(queriedProduct).isEqualTo(productProjection);
        verify(productCreateCommandHandler, times(1)).createProduct(createCommand);
        verify(getProductQueryHandler, times(1)).getProductById(productId);
    }

    @Test
    @DisplayName("Should act as orchestrator without business logic")
    void shouldActAsOrchestratorWithoutBusinessLogic() {
        // given
        when(productCreateCommandHandler.createProduct(createCommand)).thenReturn(expectedProductId);

        // when
        applicationService.createProduct(createCommand);

        // then
        // Verify that service only delegates, no transformation or validation
        verify(productCreateCommandHandler, times(1)).createProduct(createCommand);
        verifyNoMoreInteractions(productCreateCommandHandler, getProductQueryHandler);
    }

    @Test
    @DisplayName("Should handle command with minimum valid values")
    void shouldHandleCommandWithMinimumValidValues() {
        // given
        final CreateProductCommand minCommand = CreateProductCommand.builder()
                .userId(UUID.randomUUID())
                .code("MIN-001")
                .name("Minimum Product")
                .price(BigDecimal.valueOf(0.01))
                .quantity(11)
                .build();

        final ProductId minProductId = new ProductId(UUID.randomUUID());
        when(productCreateCommandHandler.createProduct(minCommand)).thenReturn(minProductId);

        // when
        final ProductId result = applicationService.createProduct(minCommand);

        // then
        assertThat(result).isEqualTo(minProductId);
        verify(productCreateCommandHandler, times(1)).createProduct(minCommand);
    }

    @Test
    @DisplayName("Should handle query for product with NOT_AVAILABLE status")
    void shouldHandleQueryForProductWithNotAvailableStatus() {
        // given
        final ProductProjection outOfStockProjection = ProductProjection.builder()
                .productId(productId)
                .userId(UUID.randomUUID())
                .status(ProductStatus.NOT_AVAILABLE)
                .code("OUT-001")
                .name("Out of Stock Product")
                .price(BigDecimal.valueOf(50.00))
                .quantity(0)
                .build();

        when(getProductQueryHandler.getProductById(productId)).thenReturn(outOfStockProjection);

        // when
        final ProductProjection result = applicationService.getProduct(productId);

        // then
        assertThat(result.status()).isEqualTo(ProductStatus.NOT_AVAILABLE);
        assertThat(result.quantity()).isEqualTo(0);
    }
}
