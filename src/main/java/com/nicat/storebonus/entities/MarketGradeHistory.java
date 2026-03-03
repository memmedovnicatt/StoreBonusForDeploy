package com.nicat.storebonus.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Long gradeId;
}