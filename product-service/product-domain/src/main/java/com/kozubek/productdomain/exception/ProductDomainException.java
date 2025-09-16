package com.kozubek.productdomain.exception;

public class ProductDomainException extends RuntimeException {
    public ProductDomainException(final String message) {
        super(message);
    }
}
