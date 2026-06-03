package com.allo.inventory.repository;

import com.allo.inventory.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory>
    findByProductIdAndWarehouseId(
            Long productId,
            Long warehouseId
    );
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
       SELECT i
       FROM Inventory i
       WHERE i.id = :id
       """)
    Optional<Inventory> findByIdForUpdate(
            @Param("id") Long id
    );
}