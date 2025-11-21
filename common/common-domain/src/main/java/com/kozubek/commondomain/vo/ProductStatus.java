package com.kozubek.commondomain.vo;

import com.kozubek.ddd.annotation.domaindrivendesign.ValueObject;

@ValueObject
public enum ProductStatus {
	AVAILABLE, NOT_AVAILABLE, LAST_PIECES
}
