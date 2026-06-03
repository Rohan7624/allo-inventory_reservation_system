package com.allo.reservation.controller;

import com.allo.reservation.dto.request.CreateReservationRequest;
import com.allo.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody
            CreateReservationRequest request) {

        return ResponseEntity.status(
                HttpStatus.CREATED)
                .body(
                        reservationService
                                .createReservation(
                                        request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirm(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService
                        .confirmReservation(id));
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<?> release(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                reservationService
                        .releaseReservation(id));
    }
}