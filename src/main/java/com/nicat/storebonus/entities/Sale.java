package com.nicat.storebonus.entities;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "sales")
public class Sale extends BaseEntity {

    String name;
    String location;

    //employer_id
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
    @ManyToOne
    @JoinColumn(name = "market_id")
    Market market;

    Date date;
    BigDecimal price;
    String currency;
}