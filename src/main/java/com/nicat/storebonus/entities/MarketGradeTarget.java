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
@Table(name = "market_grade_targets")
public class MarketGradeTarget extends BaseEntity {

    //market_id
    @ManyToOne
    @JoinColumn(name = "market_id")
    Market market;

    //grade_id
    @ManyToOne
    @JoinColumn(name = "grade_id")
    Grade grade;

    String period;
    BigDecimal minThresholdAmount;
    BigDecimal maxThresholdAmount;
}
