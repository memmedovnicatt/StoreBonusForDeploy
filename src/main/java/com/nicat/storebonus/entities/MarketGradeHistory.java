package com.nicat.storebonus.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "market_grade_histories")
public class MarketGradeHistory extends BaseEntity {

    LocalDate startDate;
    LocalDate endDate;
    BigDecimal minThreshold;
    BigDecimal maxThreshold;
    BigDecimal generalPercent;

    //grade_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    Grade grade;

    //market_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    Market market;

}