package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.SaleRequest;
import com.nicat.storebonus.dtos.response.FinalSalaryResponse;
import com.nicat.storebonus.entities.Employer;
import com.nicat.storebonus.entities.EmployerContract;
import com.nicat.storebonus.entities.Market;
import com.nicat.storebonus.entities.Sale;
import com.nicat.storebonus.mapper.SaleMapper;
import com.nicat.storebonus.repositories.EmployerContractRepository;
import com.nicat.storebonus.repositories.SaleRepository;
import com.nicat.storebonus.services.EmployerService;
import com.nicat.storebonus.services.MarketService;
import com.nicat.storebonus.services.SaleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SaleServiceImpl implements SaleService {

    SaleRepository saleRepository;
    SaleMapper saleMapper;
    EmployerService employerService;
    MarketService marketService;
    EmployerContractRepository employerContractRepository;

    @Override
    public void create(SaleRequest saleRequest) {
        Market market = marketService.checkExistsMarket(saleRequest.marketId());

        Employer employer = employerService.checkExistsEmployer(saleRequest.employerId());

        Sale savedSale = saleMapper.toSale(saleRequest);
        savedSale.setEmployer(employer);
        savedSale.setMarket(market);

        saleRepository.save(savedSale);
    }

    @Override
    public List<FinalSalaryResponse> calculateFinalSalary() {
        log.debug("calculateFinalSalary method was started");
        List<EmployerContract> list = employerContractRepository.findByLeavingDateIsNotNull();
        log.debug("list size is : {}", list.size());
        List<FinalSalaryResponse> result = new ArrayList<>();

        for (EmployerContract employerContract : list) {

            long workDays = ChronoUnit.DAYS.between(
                    employerContract.getValidFrom(),
                    employerContract.getLeavingDate());
            log.debug("workDay : {}", workDays);

            BigDecimal dailySalary = employerContract.getBaseSalary()
                    .divide(BigDecimal.valueOf(31), 2, RoundingMode.HALF_UP);
            log.debug("dailySalary : {}", dailySalary);

            BigDecimal finalSalary = dailySalary.multiply(BigDecimal.valueOf(workDays));
            log.debug("finalSalary : {}", finalSalary);

            FinalSalaryResponse dto = new FinalSalaryResponse();
            dto.setEmployeeId(employerContract.getEmployer().getId());
            dto.setEmployeeName(employerContract.getEmployer().getName());
            dto.setBaseSalary(employerContract.getBaseSalary());
            dto.setWorkedDays(workDays);
            dto.setFinalSalary(finalSalary);

            result.add(dto);
        }
        log.debug("successfully mapped to FinalSalaryResponse dto");
        return result;
    }
}
