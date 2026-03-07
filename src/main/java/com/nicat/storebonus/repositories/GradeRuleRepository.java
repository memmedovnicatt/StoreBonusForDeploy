package com.nicat.storebonus.repositories;

import com.nicat.storebonus.dtos.response.GradeRuleResponse;
import com.nicat.storebonus.entities.GradeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GradeRuleRepository extends JpaRepository<GradeRule, Long> {

    @Query("select new com.nicat.storebonus.dtos.response.GradeRuleResponse(" +
            "gbr.grade.id," +
            "gbr.position.id," +
            "gbr.employer.id," +
            "gbr.market.id," +
            "gbr.bonusPercent," +
            "gbr.amount," +
            "gbr.currency" + ")" +
            "from GradeRule gbr " +
            "where gbr.grade.id = :gradeId " +
            "and gbr.market.id = :marketId " +
            "and gbr.isActive = true")
    List<GradeRuleResponse> findByGradeIdAndMarketId(Long gradeId, Long marketId);
}