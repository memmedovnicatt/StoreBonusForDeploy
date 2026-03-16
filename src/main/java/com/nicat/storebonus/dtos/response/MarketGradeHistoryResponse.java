package com.nicat.storebonus.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MarketGradeHistoryResponse(
        String employerName,
        String employerSurname,
//        String marketName,
        BigDecimal baseSalary,
        BigDecimal bonusAmount,
        BigDecimal totalSalary,
        LocalDateTime paidAt,
        String period,
        String phoneNumber,
        String positionName
) {
}