package com.allo.reservation.entity;

import com.allo.inventory.entity.Inventory;
import com.allo.reservation.constants.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}