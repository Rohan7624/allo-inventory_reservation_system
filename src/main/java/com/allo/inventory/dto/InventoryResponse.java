package com.allo.inventory.dto;

public record InventoryResponse(

        Long inventoryId,

        Long productId,

        String productName,

        Long warehouseId,

        String warehouseName,

        Integer totalStock,

        Integer reservedStock,

        Integer availableStock
) {
}