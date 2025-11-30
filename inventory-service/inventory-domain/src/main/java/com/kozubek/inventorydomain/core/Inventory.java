package com.kozubek.inventorydomain.core;

import com.kozubek.commondomain.vo.InventoryId;
import com.kozubek.commondomain.vo.ProductId;
import com.kozubek.ddd.annotation.domaindrivendesign.AggregateRoot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AggregateRoot
@AllArgsConstructor
@Builder
@Getter
public class Inventory {

    private InventoryId id;
    private ProductId productId;
    private String code;
    private int quantityTotal;
    private int quantityReserved;
    private int quantityAvailable;

    public void initialize() {
        id = new InventoryId(UUID.randomUUID());
    }
}
