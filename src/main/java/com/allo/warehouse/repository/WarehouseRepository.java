package com.allo.warehouse.repository;

import com.allo.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository
        extends JpaRepository<Warehouse, Long> {
}