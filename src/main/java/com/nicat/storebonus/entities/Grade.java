package com.nicat.storebonus.entities;


import com.nicat.storebonus.enums.GradeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

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

    LocalDateTime deletedAt;
}