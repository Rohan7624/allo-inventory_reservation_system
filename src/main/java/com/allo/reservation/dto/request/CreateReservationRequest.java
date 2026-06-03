package com.allo.reservation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateReservationRequest(

        @NotNull
        Long inventoryId,

        @NotNull
        @Min(1)
        Integer quantity
) {
}