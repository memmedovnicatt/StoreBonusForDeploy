package com.nicat.storebonus.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "employer_grade_targets")
public class EmployerGradeTarget extends BaseEntity {

    //employer_id
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
    @ManyToOne
    @JoinColumn(name = "market_id")
    Market market;

    String period;
    BigDecimal minThresholdAmount;
    BigDecimal maxThresholdAmount;
}
