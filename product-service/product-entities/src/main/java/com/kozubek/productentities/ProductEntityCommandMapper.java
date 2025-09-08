package com.kozubek.productentities;

import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.core.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityCommandMapper {

    public ProductEntity productToProductEntity(final Product product) {
        return ProductEntity.builder()
                .id(product.getId().id())
                .userId(product.getUserId().id())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice().amount())
                .quantity(product.getQuantity())
                .status(product.getStatus())
                .build();
    }

    public Product productEntityToProduct(final ProductEntity productEntity) {
        return Product.builder()
                .id(new ProductId(productEntity.getId()))
                .userId(new UserId(productEntity.getUserId()))
                .code(productEntity.getCode())
                .name(productEntity.getName())
                .price(new Money(productEntity.getPrice()))
                .quantity(productEntity.getQuantity())
                .status(productEntity.getStatus())
                .build();
    }
}
