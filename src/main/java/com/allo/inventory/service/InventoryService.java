package com.allo.inventory.service;

import com.allo.inventory.dto.InventoryResponse;
import com.allo.inventory.entity.Inventory;
import com.allo.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InventoryResponse mapToResponse(
            Inventory inventory
    ) {

        return new InventoryResponse(

                inventory.getId(),

                inventory.getProduct().getId(),

                inventory.getProduct().getName(),

                inventory.getWarehouse().getId(),

                inventory.getWarehouse().getName(),

                inventory.getTotalStock(),

                inventory.getReservedStock(),

                inventory.getTotalStock()
                        - inventory.getReservedStock()
        );
    }
}