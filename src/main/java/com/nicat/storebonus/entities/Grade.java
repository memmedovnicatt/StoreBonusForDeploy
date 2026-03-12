package com.nicat.storebonus.entities;


import com.nicat.storebonus.enums.GradeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "grades")
public class Grade extends BaseEntity {

    String name;

    @Enumerated(EnumType.STRING)
    GradeType gradeType;

    BigDecimal generalPercent;

    LocalDateTime deletedAt;
    BigDecimal minPercent;
    BigDecimal middlePercent;
    BigDecimal maxPercent;
}