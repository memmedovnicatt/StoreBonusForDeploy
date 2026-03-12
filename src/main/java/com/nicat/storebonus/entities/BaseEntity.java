package com.nicat.storebonus.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    @LastModifiedDate
    LocalDateTime updatedAt;
    boolean isActive;

    @PrePersist
    protected void onCreate() {
        if (!isActive) {
            isActive = true;
        }
        if (this instanceof Grade g) {
            if (g.getMinPercent() == null) {
                g.setMinPercent(BigDecimal.ZERO);
            }
            if (g.getMiddlePercent() == null) {
                g.setMiddlePercent(BigDecimal.ZERO);
            }
            if (g.getMaxPercent() == null) {
                g.setMaxPercent(BigDecimal.ZERO);
            }
        }
    }
}