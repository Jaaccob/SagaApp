package com.kozubek.productapplication.query;

import com.kozubek.commondomain.vo.ProductStatus;
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
@DisplayName("GetProductQueryHandler Tests")
class GetProductQueryHandlerTest {

    @Mock
    private ProductQueryRepository productQueryRepository;

    @InjectMocks
    private GetProductQueryHandler queryHandler;

    private UUID productId;
    private ProductProjection expectedProjection;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        expectedProjection = ProductProjection.builder()
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
    @DisplayName("Should get product projection by ID successfully")
    void shouldGetProductProjectionByIdSuccessfully() {
        // given
        when(productQueryRepository.getProductProjection(productId)).thenReturn(expectedProjection);

        // when
        final ProductProjection result = queryHandler.getProductById(productId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedProjection);
        assertThat(result.productId()).isEqualTo(productId);
        assertThat(result.code()).isEqualTo("PROD-001");
        assertThat(result.name()).isEqualTo("Test Product");
        assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
        assertThat(result.quantity()).isEqualTo(20);
        assertThat(result.status()).isEqualTo(ProductStatus.AVAILABLE);

        verify(productQueryRepository, times(1)).getProductProjection(productId);
    }

    @Test
    @DisplayName("Should delegate to repository for data retrieval")
    void shouldDelegateToRepositoryForDataRetrieval() {
        // given
        when(productQueryRepository.getProductProjection(productId)).thenReturn(expectedProjection);

        // when
        queryHandler.getProductById(productId);

        // then
        verify(productQueryRepository, times(1)).getProductProjection(productId);
        verifyNoMoreInteractions(productQueryRepository);
    }

    @Test
    @DisplayName("Should handle product with NOT_AVAILABLE status")
    void shouldHandleProductWithNotAvailableStatus() {
        // given
        final ProductProjection outOfStockProjection = ProductProjection.builder()
                .productId(productId)
                .userId(UUID.randomUUID())
                .status(ProductStatus.NOT_AVAILABLE)
                .code("PROD-002")
                .name("Out of Stock Product")
                .price(BigDecimal.valueOf(50.00))
                .quantity(0)
                .build();

        when(productQueryRepository.getProductProjection(productId)).thenReturn(outOfStockProjection);

        // when
        final ProductProjection result = queryHandler.getProductById(productId);

        // then
        assertThat(result.status()).isEqualTo(ProductStatus.NOT_AVAILABLE);
        assertThat(result.quantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should propagate exception when repository throws exception")
    void shouldPropagateExceptionWhenRepositoryThrowsException() {
        // given
        when(productQueryRepository.getProductProjection(any(UUID.class)))
                .thenThrow(new RuntimeException("Product not found"));

        // when & then
        assertThatThrownBy(() -> queryHandler.getProductById(productId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Product not found");

        verify(productQueryRepository, times(1)).getProductProjection(productId);
    }

    @Test
    @DisplayName("Should handle multiple consecutive queries")
    void shouldHandleMultipleConsecutiveQueries() {
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

        when(productQueryRepository.getProductProjection(productId1)).thenReturn(projection1);
        when(productQueryRepository.getProductProjection(productId2)).thenReturn(projection2);

        // when
        final ProductProjection result1 = queryHandler.getProductById(productId1);
        final ProductProjection result2 = queryHandler.getProductById(productId2);

        // then
        assertThat(result1).isEqualTo(projection1);
        assertThat(result2).isEqualTo(projection2);
        verify(productQueryRepository, times(1)).getProductProjection(productId1);
        verify(productQueryRepository, times(1)).getProductProjection(productId2);
    }

    @Test
    @DisplayName("Should return projection with all fields populated")
    void shouldReturnProjectionWithAllFieldsPopulated() {
        // given
        final UUID userId = UUID.randomUUID();
        final ProductProjection completeProjection = ProductProjection.builder()
                .productId(productId)
                .userId(userId)
                .status(ProductStatus.AVAILABLE)
                .code("COMPLETE-001")
                .name("Complete Product")
                .price(BigDecimal.valueOf(999.99))
                .quantity(100)
                .build();

        when(productQueryRepository.getProductProjection(productId)).thenReturn(completeProjection);

        // when
        final ProductProjection result = queryHandler.getProductById(productId);

        // then
        assertThat(result.productId()).isNotNull();
        assertThat(result.userId()).isNotNull();
        assertThat(result.status()).isNotNull();
        assertThat(result.code()).isNotNull();
        assertThat(result.name()).isNotNull();
        assertThat(result.price()).isNotNull();
        assertThat(result.quantity()).isNotNull();
    }

    @Test
    @DisplayName("Should handle product with minimum price")
    void shouldHandleProductWithMinimumPrice() {
        // given
        final ProductProjection cheapProjection = ProductProjection.builder()
                .productId(productId)
                .userId(UUID.randomUUID())
                .status(ProductStatus.AVAILABLE)
                .code("CHEAP-001")
                .name("Cheap Product")
                .price(BigDecimal.valueOf(0.01))
                .quantity(15)
                .build();

        when(productQueryRepository.getProductProjection(productId)).thenReturn(cheapProjection);

        // when
        final ProductProjection result = queryHandler.getProductById(productId);

        // then
        assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(0.01));
    }

    @Test
    @DisplayName("Should handle product with large quantity")
    void shouldHandleProductWithLargeQuantity() {
        // given
        final ProductProjection bulkProjection = ProductProjection.builder()
                .productId(productId)
                .userId(UUID.randomUUID())
                .status(ProductStatus.AVAILABLE)
                .code("BULK-001")
                .name("Bulk Product")
                .price(BigDecimal.valueOf(10.00))
                .quantity(10000)
                .build();

        when(productQueryRepository.getProductProjection(productId)).thenReturn(bulkProjection);

        // when
        final ProductProjection result = queryHandler.getProductById(productId);

        // then
        assertThat(result.quantity()).isEqualTo(10000);
    }
}
