package com.nicat.storebonus.dtos.request;

import jakarta.validation.constraints.NotBlank;

public record PositionRequest(
        @NotBlank(message = "Position name can't null")
        String name
) {
}