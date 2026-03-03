package com.nicat.storebonus.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
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
    Date validFrom;

    Date validTo;

    BigDecimal baseSalary;
    String currency;

    //employer_id
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
    @ManyToOne
    @JoinColumn(name = "market_id")
    Market market;

    //market_id
    @ManyToOne
    @JoinColumn(name = "position_id")
    Position position;
}