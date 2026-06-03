package com.allo.product.dto;

import java.util.UUID;

public record ProductResponse(
        Long id,
        String name,
        String description
) {
}