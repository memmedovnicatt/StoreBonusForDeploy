package com.nicat.storebonus.repositories;

import com.nicat.storebonus.entities.MarketGradeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MarketGradeHistoryRepository extends JpaRepository<MarketGradeHistory, Long> {


    @Query("select mgh from MarketGradeHistory mgh " +
            "join mgh.market m " +
            "where m.id=:marketId")
    Optional<MarketGradeHistory> findByMarketIdAndIsActive(Long marketId, boolean isActive);
}