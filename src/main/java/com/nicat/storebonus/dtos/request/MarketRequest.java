package com.nicat.storebonus.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MarketRequest(
        @NotBlank(message = "Name can not null")
        String name,

        @NotBlank(message = "Location can not null")
        String location,

        @NotNull(message = "WareHouseId can not null")
        @Positive
        Long wareHouseId,

        @NotNull(message = "GradeId can not null")
        @Positive
        Long gradeId
) {
}