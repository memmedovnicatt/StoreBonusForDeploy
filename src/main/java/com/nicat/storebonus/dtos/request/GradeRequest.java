package com.nicat.storebonus.dtos.request;

import com.nicat.storebonus.enums.GradeType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record GradeRequest(
        @NotNull(message = "Type name of grade can't null")
        GradeType gradeType,

        String name,
        BigDecimal generalPercent,
        BigDecimal minPercent,
        BigDecimal maxPercent
) {
}