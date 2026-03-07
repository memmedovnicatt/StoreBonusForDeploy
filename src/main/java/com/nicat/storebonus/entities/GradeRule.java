package com.nicat.storebonus.entities;


import jakarta.persistence.*;
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
@Table(name = "grade_rules")
public class GradeRule extends BaseEntity {
    //grade_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    Grade grade;

    //position_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    Position position;

    //market_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    Market market;

    //employee_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    Employer employer;

    Double bonusPercent;
    BigDecimal amount;
    String currency;
}