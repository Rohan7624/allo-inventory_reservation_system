package com.allo.reservation.repository;

import com.allo.reservation.constants.ReservationStatus;
import com.allo.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStatusAndExpiresAtBefore(
            ReservationStatus status,
            LocalDateTime time
    );
}