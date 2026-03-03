package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.entities.Employer;
import com.nicat.storebonus.entities.EmployerContract;
import com.nicat.storebonus.entities.Market;
import com.nicat.storebonus.entities.Position;
import com.nicat.storebonus.mapper.EmployerContractMapper;
import com.nicat.storebonus.repositories.EmployerContractRepository;
import com.nicat.storebonus.services.EmployerContractService;
import com.nicat.storebonus.services.EmployerService;
import com.nicat.storebonus.services.MarketService;
import com.nicat.storebonus.services.PositionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerContractServiceImpl implements EmployerContractService {

    EmployerContractRepository employerContractRepository;
    EmployerService employerService;
    MarketService marketService;
    PositionService positionService;
    EmployerContractMapper employerContractMapper;

    @Override
    public void create(EmployerContractRequest employerContractRequest) {
        log.info("create method basladi ");

        Position position = positionService
                .checkExistsPosition(employerContractRequest.positionId());

        log.info("position tapildi");

        Market market = marketService
                .checkExistsMarket(employerContractRequest.marketId());

        Employer employer = employerService
                .checkExistsEmployer(employerContractRequest.employerId());

        EmployerContract savedEmployerContract = employerContractMapper
                .toEmployerContract(employerContractRequest);

        savedEmployerContract.setEmployer(employer);
        savedEmployerContract.setPosition(position);
        savedEmployerContract.setMarket(market);

        employerContractRepository.save(savedEmployerContract);
    }
}
