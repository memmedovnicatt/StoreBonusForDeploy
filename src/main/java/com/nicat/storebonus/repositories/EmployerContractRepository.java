package com.nicat.storebonus.repositories;

import com.nicat.storebonus.dtos.response.EmployerContractResponse;
import com.nicat.storebonus.entities.EmployerContract;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployerContractRepository extends JpaRepository<EmployerContract, Long> {

//
////    @Query("select new com.nicat.storebonus.dtos.response.EmployerContractResponse(" +
////            "null ," +
////            "ecr.position.id," +
////            "ecr.market.id," +
////            "ecr.employer.id," +
////            "ecr.baseSalary," +
////            "ecr.validFrom," +
////            "ecr.validTo," +
////            "ecr.currency" + ")" +
////            "from EmployerContract ecr " +
////            "where ecr.employer.id= :employeeId " +
////            "and ecr.isActive= true")
//    EmployerContractResponse findByEmployerIdAndIsActive(Long employeeId, boolean isActive);


    List<EmployerContract> findAllByEmployerIdInAndIsActive(List<Long> employeeIds, boolean isActive);

}
