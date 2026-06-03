package com.allo.inventory.controller;

import com.allo.inventory.dto.InventoryResponse;
import com.allo.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryResponse>
    getInventory() {

        return inventoryService.getAllInventory();
    }
}