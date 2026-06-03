package com.allo.warehouse.service;

import com.allo.warehouse.dto.WarehouseResponse;
import com.allo.warehouse.entity.Warehouse;
import com.allo.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<WarehouseResponse> getAllWarehouses() {

        return warehouseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private WarehouseResponse mapToResponse(Warehouse warehouse) {

        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getLocation()
        );
    }
}