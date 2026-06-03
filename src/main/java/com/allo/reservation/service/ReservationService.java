package com.allo.reservation.service;

import com.allo.exception.InsufficientStockException;
import com.allo.exception.ReservationExpiredException;
import com.allo.exception.ReservationNotFoundException;
import com.allo.inventory.entity.Inventory;
import com.allo.inventory.repository.InventoryRepository;
import com.allo.reservation.constants.ReservationStatus;
import com.allo.reservation.dto.request.CreateReservationRequest;
import com.allo.reservation.dto.response.ReservationResponse;
import com.allo.reservation.entity.Reservation;
import com.allo.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse createReservation(
            CreateReservationRequest request) {

        Inventory inventory =
                inventoryRepository
                        .findByIdForUpdate(request.inventoryId())
                        .orElseThrow(() ->
                                new RuntimeException("Inventory not found"));

        int availableStock =
                inventory.getTotalStock()
                        - inventory.getReservedStock();

        if (availableStock < request.quantity()) {
            throw new InsufficientStockException(
                    "Not enough stock available");
        }

        inventory.setReservedStock(
                inventory.getReservedStock()
                        + request.quantity());

        Reservation reservation =
                Reservation.builder()
                        .inventory(inventory)
                        .quantity(request.quantity())
                        .status(ReservationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(10))
                        .build();

        reservationRepository.save(reservation);

        return mapToResponse(reservation);
    }

    @Transactional
    public ReservationResponse confirmReservation(
            Long reservationId) {

        Reservation reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(
                                () -> new ReservationNotFoundException(
                                        "Reservation not found"));

        if (reservation.getStatus()
                == ReservationStatus.RELEASED) {

            throw new ReservationExpiredException(
                    "Reservation already released");
        }

        if (reservation.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            releaseExpiredReservation(reservation);

            throw new ReservationExpiredException(
                    "Reservation expired");
        }

        reservation.setStatus(
                ReservationStatus.CONFIRMED);

        reservationRepository.save(reservation);

        return mapToResponse(reservation);
    }

    @Transactional
    public ReservationResponse releaseReservation(
            Long reservationId) {

        Reservation reservation =
                reservationRepository
                        .findById(reservationId)
                        .orElseThrow(
                                () -> new ReservationNotFoundException(
                                        "Reservation not found"));

        if (reservation.getStatus()
                == ReservationStatus.RELEASED) {

            return mapToResponse(reservation);
        }

        Inventory inventory =
                reservation.getInventory();

        inventory.setReservedStock(
                inventory.getReservedStock()
                        - reservation.getQuantity());

        reservation.setStatus(
                ReservationStatus.RELEASED);

        reservationRepository.save(reservation);

        return mapToResponse(reservation);
    }

    private void releaseExpiredReservation(
            Reservation reservation) {

        Inventory inventory =
                reservation.getInventory();

        inventory.setReservedStock(
                inventory.getReservedStock()
                        - reservation.getQuantity());

        reservation.setStatus(
                ReservationStatus.RELEASED);

        reservationRepository.save(reservation);
    }

    private ReservationResponse mapToResponse(
            Reservation reservation) {

        return new ReservationResponse(
                reservation.getId(),
                reservation.getStatus().name(),
                reservation.getQuantity(),
                reservation.getExpiresAt()
        );
    }
}