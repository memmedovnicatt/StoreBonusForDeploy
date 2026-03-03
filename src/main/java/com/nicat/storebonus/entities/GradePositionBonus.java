package com.nicat.storebonus.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.yaml.snakeyaml.error.Mark;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "grade_position_bonusses")
public class GradePositionBonus extends BaseEntity {
    //grade_id
    @ManyToOne
    @JoinColumn(name = "grade_id")
    Grade grade;

    //position_id
    @ManyToOne
    @JoinColumn(name = "position_id")
    Position position;

    //market_id
    @ManyToOne
    @JoinColumn(name = "market_id")
    Market market;

    Double bonusPercent;
    Double amount;
}
