package com.nicat.storebonus.dtos.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinalSalaryResponse {
    private Long employeeId;
    private String employeeName;
    private BigDecimal baseSalary;
    private Long workedDays;
    private BigDecimal finalSalary;
}
