package com.nicat.storebonus.repositories;

import com.nicat.storebonus.dtos.request.GradeCalculationRequest;
import com.nicat.storebonus.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("select sum(s.price) from Sale s " +
            "WHERE s.market.id=:marketId " +
            "AND s.date >= :startDate")
    BigDecimal sumPriceByMarketIdAndDate(
            @Param("marketId") Long marketId,
            @Param("startDate") LocalDate startDate
    );
}
