package com.allo.reservation.dto.response;

import java.time.LocalDateTime;

public record ReservationResponse(

        Long reservationId,

        String status,

        Integer quantity,

        LocalDateTime expiresAt
) {
}