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
@Table(name = "employer_contracts")
public class EmployerContract extends BaseEntity {
    LocalDate validFrom;

    LocalDate validTo;
    LocalDate leavingDate; //also include here transfer from one market to another market

    BigDecimal baseSalary;
    String currency;

    //employer_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    Market market;

    //position_id
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    Position position;
}