package com.kozubek.productdomain.core;

import com.kozubek.commondomain.model.AggregateRoot;
import com.kozubek.commondomain.vo.Money;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.commondomain.vo.ProductStatus;
import com.kozubek.commondomain.vo.UserId;
import com.kozubek.productdomain.exception.ProductDomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class Product implements AggregateRoot {

    private ProductId id;
    private UserId userId;
    private String code;
    private String name;
    private Money price;
    private int quantity;
    private ProductStatus status;

    public void inicjalze() {
        id = new ProductId(UUID.randomUUID());
        status = ProductStatus.AVAILABLE;
    }

    public boolean isAvailableStatus() {
        return status == ProductStatus.AVAILABLE;
    }

    public void validate() {
        validatePrice();
        validateQuantity();
    }

    public void validatePrice() {
        if (!price.isGreaterThanZero()) {
            throw new ProductDomainException("Product price: " + price.amount() + " must be greated than zero");
        }
    }

    public void validateQuantity() {
        if (quantity < 1) {
            throw new ProductDomainException("Product quantity: " + quantity + " must be greater than zero");
        }

        if (quantity < 10 && isAvailableStatus()) {
            throw new ProductDomainException("Product quantity: " + quantity + " must be greater than 10");
        }
    }
}
