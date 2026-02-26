package com.nicat.storebonus.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "employers")
@EntityListeners(AuditingEntityListener.class)
public class Employer extends BaseEntity {
    String name;
    String surname;
    String mail;
    byte age;

    LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "position_id")
    Position position;
}