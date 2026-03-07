package com.nicat.storebonus.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "employer_contracts")
public class EmployerContract extends BaseEntity {
    LocalDate validFrom;

    LocalDate validTo;

    BigDecimal baseSalary;
    String currency;

    //employer_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    Market market;

    //position_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    Position position;
}