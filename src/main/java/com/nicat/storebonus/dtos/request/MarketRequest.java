package com.nicat.storebonus.dtos.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record MarketRequest(
        @NotBlank(message = "Name can not null")
        String name,

        @NotBlank(message = "Location can not null")
        String location,

        @NotNull(message = "WareHouseId can not null")
        @Positive
        Long wareHouseId,

        @Positive
        Long gradeId,

        BigDecimal minThreshold,
        BigDecimal maxThreshold
) {
}