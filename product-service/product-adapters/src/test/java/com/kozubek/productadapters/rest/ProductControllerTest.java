package com.kozubek.productadapters.rest;

import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.productadapters.rest.dto.CreateProductRequest;
import com.kozubek.productadapters.rest.dto.CreateProductResponse;
import com.kozubek.productadapters.rest.dto.GetDetailsProductResponse;
import com.kozubek.productapplication.ProductApplicationService;
import com.kozubek.productapplication.command.dto.CreateProductCommand;
import com.kozubek.productapplication.query.dto.ProductProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController Unit Tests")
class ProductControllerTest {

    @Mock
    private ProductCommandRestMapper productCommandRestMapper;

    @Mock
    private ProductApplicationService productApplicationService;

    @Mock
    private ProductQueryRestMapper productQueryRestMapper;

    @InjectMocks
    private ProductController controller;

    @Captor
    private ArgumentCaptor<CreateProductCommand> commandCaptor;

    @Captor
    private ArgumentCaptor<UUID> productIdCaptor;

    private CreateProductRequest createRequest;
    private CreateProductCommand createCommand;
    private UUID productId;
    private ProductId expectedProductId;
    private ProductProjection productProjection;
    private GetDetailsProductResponse expectedResponse;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        final UUID userId = UUID.randomUUID();

        createRequest = new CreateProductRequest(
                userId,
                "PROD-001",
                "Test Product",
                BigDecimal.valueOf(99.99),
                50
        );

        createCommand = CreateProductCommand.builder()
                .userId(userId)
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(50)
                .build();

        expectedProductId = new ProductId(productId);

        productProjection = ProductProjection.builder()
                .productId(productId)
                .userId(userId)
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(50)
                .status(ProductStatus.AVAILABLE)
                .build();

        expectedResponse = GetDetailsProductResponse.builder()
                .productId(productId)
                .userId(userId)
                .code("PROD-001")
                .name("Test Product")
                .price(BigDecimal.valueOf(99.99))
                .quantity(50)
                .status(ProductStatus.AVAILABLE)
                .build();
    }

    @Test
    @DisplayName("Should create product and return response with product ID")
    void shouldCreateProductAndReturnResponse() {
        // given
        when(productCommandRestMapper.createProductRequestToCreateProductCommand(createRequest))
                .thenReturn(createCommand);
        when(productApplicationService.createProduct(createCommand))
                .thenReturn(expectedProductId);

        // when
        final ResponseEntity<CreateProductResponse> response = controller.createProduct(createRequest);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().productId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should use command mapper when creating product")
    void shouldUseCommandMapperWhenCreatingProduct() {
        // given
        when(productCommandRestMapper.createProductRequestToCreateProductCommand(createRequest))
                .thenReturn(createCommand);
        when(productApplicationService.createProduct(any(CreateProductCommand.class)))
                .thenReturn(expectedProductId);

        // when
        controller.createProduct(createRequest);

        // then
        verify(productCommandRestMapper).createProductRequestToCreateProductCommand(createRequest);
    }

    @Test
    @DisplayName("Should delegate product creation to application service")
    void shouldDelegateProductCreationToApplicationService() {
        // given
        when(productCommandRestMapper.createProductRequestToCreateProductCommand(createRequest))
                .thenReturn(createCommand);
        when(productApplicationService.createProduct(createCommand))
                .thenReturn(expectedProductId);

        // when
        controller.createProduct(createRequest);

        // then
        verify(productApplicationService).createProduct(commandCaptor.capture());
        final CreateProductCommand capturedCommand = commandCaptor.getValue();
        assertThat(capturedCommand).isEqualTo(createCommand);
    }

    @Test
    @DisplayName("Should get product details and return response")
    void shouldGetProductDetailsAndReturnResponse() {
        // given
        when(productApplicationService.getProduct(productId))
                .thenReturn(productProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(productProjection))
                .thenReturn(expectedResponse);

        // when
        final ResponseEntity<GetDetailsProductResponse> response = controller.getDetailsProduct(productId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().productId()).isEqualTo(productId);
        assertThat(response.getBody().code()).isEqualTo("PROD-001");
        assertThat(response.getBody().name()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should delegate get product to application service")
    void shouldDelegateGetProductToApplicationService() {
        // given
        when(productApplicationService.getProduct(productId))
                .thenReturn(productProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(any(ProductProjection.class)))
                .thenReturn(expectedResponse);

        // when
        controller.getDetailsProduct(productId);

        // then
        verify(productApplicationService).getProduct(productIdCaptor.capture());
        assertThat(productIdCaptor.getValue()).isEqualTo(productId);
    }

    @Test
    @DisplayName("Should use query mapper when getting product details")
    void shouldUseQueryMapperWhenGettingProductDetails() {
        // given
        when(productApplicationService.getProduct(productId))
                .thenReturn(productProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(productProjection))
                .thenReturn(expectedResponse);

        // when
        controller.getDetailsProduct(productId);

        // then
        verify(productQueryRestMapper).productToGetDetailsProductResponse(productProjection);
    }

    @Test
    @DisplayName("Should return all product fields in get details response")
    void shouldReturnAllProductFieldsInGetDetailsResponse() {
        // given
        when(productApplicationService.getProduct(productId))
                .thenReturn(productProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(productProjection))
                .thenReturn(expectedResponse);

        // when
        final ResponseEntity<GetDetailsProductResponse> response = controller.getDetailsProduct(productId);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().productId()).isEqualTo(productId);
        assertThat(response.getBody().userId()).isEqualTo(productProjection.userId());
        assertThat(response.getBody().code()).isEqualTo(productProjection.code());
        assertThat(response.getBody().name()).isEqualTo(productProjection.name());
        assertThat(response.getBody().price()).isEqualByComparingTo(productProjection.price());
        assertThat(response.getBody().quantity()).isEqualTo(productProjection.quantity());
        assertThat(response.getBody().status()).isEqualTo(productProjection.status());
    }

    @Test
    @DisplayName("Should handle different product IDs correctly")
    void shouldHandleDifferentProductIdsCorrectly() {
        // given
        final UUID differentProductId = UUID.randomUUID();
        final ProductProjection differentProjection = ProductProjection.builder()
                .productId(differentProductId)
                .userId(UUID.randomUUID())
                .code("DIFF-001")
                .name("Different Product")
                .price(BigDecimal.valueOf(199.99))
                .quantity(25)
                .status(ProductStatus.AVAILABLE)
                .build();
        final GetDetailsProductResponse differentResponse = GetDetailsProductResponse.builder()
                .productId(differentProductId)
                .userId(differentProjection.userId())
                .code(differentProjection.code())
                .name(differentProjection.name())
                .price(differentProjection.price())
                .quantity(differentProjection.quantity())
                .status(differentProjection.status())
                .build();

        when(productApplicationService.getProduct(differentProductId))
                .thenReturn(differentProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(differentProjection))
                .thenReturn(differentResponse);

        // when
        final ResponseEntity<GetDetailsProductResponse> response = controller.getDetailsProduct(differentProductId);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().productId()).isEqualTo(differentProductId);
        assertThat(response.getBody().code()).isEqualTo("DIFF-001");
        verify(productApplicationService).getProduct(eq(differentProductId));
    }

    @Test
    @DisplayName("Should verify all interactions when creating product")
    void shouldVerifyAllInteractionsWhenCreatingProduct() {
        // given
        when(productCommandRestMapper.createProductRequestToCreateProductCommand(createRequest))
                .thenReturn(createCommand);
        when(productApplicationService.createProduct(createCommand))
                .thenReturn(expectedProductId);

        // when
        controller.createProduct(createRequest);

        // then
        verify(productCommandRestMapper, times(1)).createProductRequestToCreateProductCommand(createRequest);
        verify(productApplicationService, times(1)).createProduct(createCommand);
        verifyNoMoreInteractions(productCommandRestMapper, productApplicationService);
    }

    @Test
    @DisplayName("Should verify all interactions when getting product details")
    void shouldVerifyAllInteractionsWhenGettingProductDetails() {
        // given
        when(productApplicationService.getProduct(productId))
                .thenReturn(productProjection);
        when(productQueryRestMapper.productToGetDetailsProductResponse(productProjection))
                .thenReturn(expectedResponse);

        // when
        controller.getDetailsProduct(productId);

        // then
        verify(productApplicationService, times(1)).getProduct(productId);
        verify(productQueryRestMapper, times(1)).productToGetDetailsProductResponse(productProjection);
        verifyNoMoreInteractions(productApplicationService, productQueryRestMapper);
    }
}
