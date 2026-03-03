package com.nicat.storebonus.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;

public record EmployerContractRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date validFrom,

        @JsonFormat(pattern = "yyyy-MM-dd")
        Date validTo,

        @Min(0)
        BigDecimal baseSalary,

        String currency,

        @NotNull
        Long employerId,

        @NotNull
        Long marketId,

        @NotNull
        Long positionId
) {
}
