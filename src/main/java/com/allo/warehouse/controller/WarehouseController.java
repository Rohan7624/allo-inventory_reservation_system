package com.allo.warehouse.controller;

import com.allo.warehouse.dto.WarehouseResponse;
import com.allo.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseService.getAllWarehouses();
    }
}