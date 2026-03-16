package com.nicat.storebonus.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "grade_histories")
public class GradeHistory extends BaseEntity {

    //employee_id
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;

    BigDecimal baseSalary;
    BigDecimal bonusAmount;
    BigDecimal totalSalary;
    LocalDateTime paidAt;
    String period;
}
