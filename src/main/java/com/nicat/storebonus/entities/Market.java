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
@Table(name = "markets")
public class Market extends BaseEntity {

    String name;
    String location;

    LocalDateTime deletedAt;

    //warehouse_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ware_house_id")
    WareHouse wareHouse;
}