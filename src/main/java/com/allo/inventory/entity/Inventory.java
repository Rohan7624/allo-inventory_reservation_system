package com.allo.inventory.entity;

import com.allo.product.entity.Product;
import com.allo.warehouse.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "product_id",
                                "warehouse_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    private Integer totalStock;

    private Integer reservedStock;
}