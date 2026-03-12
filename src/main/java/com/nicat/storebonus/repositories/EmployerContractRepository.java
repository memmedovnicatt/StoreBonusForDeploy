package com.nicat.storebonus.repositories;

import com.nicat.storebonus.dtos.response.EmployerContractResponse;
import com.nicat.storebonus.entities.EmployerContract;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployerContractRepository extends JpaRepository<EmployerContract, Long> {
    List<EmployerContract> findAllByEmployerIdInAndIsActive(List<Long> employeeIds, boolean isActive);

    int countByMarketIdAndIsActive(Long marketId, boolean isActive);

    @Query("""
            SELECT new com.nicat.storebonus.dtos.response.EmployerContractResponse(
                null,
                e.position.id,
                e.market.id,
                e.employer.id,
                e.baseSalary,
                null,
                null,
                e.currency,
                e.validFrom,
                e.validTo
            )
            FROM EmployerContract e
            WHERE e.employer.id NOT IN :employerIds
            AND e.market.id=:marketId
            AND e.isActive=true
            """)
    List<EmployerContractResponse> findByEmployerIdNotIn(@Param("employerIds") List<Long> employerIds,
                                                         Long marketId,
                                                         boolean isActive);


    @Query("""
            SELECT new com.nicat.storebonus.dtos.response.EmployerContractResponse(
                null,
                e.position.id,
                e.market.id,
                e.employer.id,
                e.baseSalary,
                null,
                null,
                e.currency,
                e.validFrom,
                e.validTo
            )
            FROM EmployerContract e
            WHERE e.market.id=:marketId
            AND e.isActive=true
            """)
    List<EmployerContractResponse> findAllByMarketIdAndIsActive(Long marketId,boolean isActive);
}