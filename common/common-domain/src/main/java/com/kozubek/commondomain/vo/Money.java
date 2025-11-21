package com.kozubek.commondomain.vo;

import com.kozubek.ddd.annotation.domaindrivendesign.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ValueObject
public record Money(BigDecimal amount) {

    public Money(final BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException();
        }
        this.amount = setScale(amount);
    }

    public Money add(final Money money) {
        return new Money(setScale(this.amount.add(money.amount())));
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    private BigDecimal setScale(final BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }
}
