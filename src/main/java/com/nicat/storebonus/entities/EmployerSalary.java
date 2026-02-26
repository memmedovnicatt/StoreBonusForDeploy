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
@Table(name = "employer_salaries")
@EntityListeners(AuditingEntityListener.class)
public class EmployerSalary extends BaseEntity {
    Date validFrom;

    Date validTo;

    BigDecimal baseSalary;
    //maybe implemented currency for calculated real time and convert AZN

    //employer_id
    @ManyToOne
    @JoinColumn(name = "employer_id")
    Employer employer;

    //market_id
}