package com.nicat.storebonus.services.impl;

import aQute.bnd.annotation.jpms.Open;
import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.entities.Employer;
import com.nicat.storebonus.entities.EmployerContract;
import com.nicat.storebonus.entities.Market;
import com.nicat.storebonus.exceptions.handler.ResourceNotFoundException;
import com.nicat.storebonus.mapper.EmployerContractMapper;
import com.nicat.storebonus.repositories.EmployerContractRepository;
import com.nicat.storebonus.services.EmployerContractService;
import com.nicat.storebonus.services.EmployerService;
import com.nicat.storebonus.services.MarketService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerContractServiceImpl implements EmployerContractService {

    EmployerContractRepository employerContractRepository;
    EmployerService employerService;
    MarketService marketService;
    EmployerContractMapper employerContractMapper;

    @Override
    public void createContract(EmployerContractRequest employerContractRequest) {
        log.debug("createContract method was started for employee contract service");

        Market market = marketService
                .checkExistsMarket(employerContractRequest.marketId());
        log.debug("marketId: {}", market.getId());

        Employer employer = employerService
                .checkExistsEmployer(employerContractRequest.employerId());
        log.debug("employeeId: {}", employer.getId());

        EmployerContract savedEmployerContract = employerContractMapper
                .toEmployerContract(employerContractRequest);

        savedEmployerContract.setEmployer(employer);
        savedEmployerContract.setPosition(employer.getPosition());
        savedEmployerContract.setMarket(market);

        employerContractRepository.save(savedEmployerContract);
        log.debug("save successfully for employee contract");
    }

    @Override
    public void deactivateEmployee(Long employeeId) {
        log.debug("deactivateEmployee method was started");
        EmployerContract employeeContract = employerContractRepository.findByEmployerIdAndIsActive(employeeId, true)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        log.debug("employeeContract : {}", employeeContract.getEmployer().getId());
        employeeContract.setActive(false);
        employeeContract.setLeavingDate(LocalDate.now());
        log.debug("employee status changed");
        employerContractRepository.save(employeeContract);
        log.debug("employeeContract was saved");
    }
}
