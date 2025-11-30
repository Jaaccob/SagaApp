package com.kozubek.inventorydomain.core;

import com.kozubek.commondomain.vo.InventoryId;
import com.kozubek.commondomain.vo.InventoryLocationId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class InventoryLocation {

    private InventoryLocationId id;
    private InventoryId inventoryId;
    private String warehouseId; //TODO change in future to UUID
    private String aisle;
    private String shelf;
    private boolean isIncoming;
    private int quantity;

    public void initialize(int quantity) {
        id = new InventoryLocationId(UUID.randomUUID());
        warehouseId = "MAIN-HUB";
        isIncoming = true;
        this.quantity = quantity;
    }
}
