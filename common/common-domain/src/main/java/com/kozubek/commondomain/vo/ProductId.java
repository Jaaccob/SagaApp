package com.kozubek.commondomain.vo;

import com.kozubek.ddd.annotation.domaindrivendesign.ValueObject;

import java.util.UUID;

@ValueObject
public record ProductId(UUID id) {
}
