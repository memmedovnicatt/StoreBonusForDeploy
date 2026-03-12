package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.GradeCalculationRequest;
import com.nicat.storebonus.dtos.request.GradeRequest;
import com.nicat.storebonus.dtos.response.EmployerContractResponse;
import com.nicat.storebonus.dtos.response.GradeRuleResponse;
import com.nicat.storebonus.entities.*;
import com.nicat.storebonus.enums.GradeType;
import com.nicat.storebonus.exceptions.handler.ResourceNotFoundException;
import com.nicat.storebonus.exceptions.handler.TargetNotReachedException;
import com.nicat.storebonus.repositories.*;
import com.nicat.storebonus.services.GradeService;
import com.nicat.storebonus.utility.ThresholdsUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradeServiceImpl implements GradeService {

    GradeRepository gradeRepository;
    SaleRepository saleRepository;
    MarketGradeHistoryRepository marketGradeHistoryRepository;
    GradeRuleRepository gradeRuleRepository;
    EmployerContractRepository employerContractRepository;
    EmployerRepository employerRepository;
    GradeHistoryRepository gradeHistoryRepository;
    ThresholdsUtil thresholdsUtil;

    @Override
    public void create(GradeRequest gradeRequest) {
        Grade grade = Grade.builder()
                .gradeType(gradeRequest.gradeType())
                .name(gradeRequest.name())
                .generalPercent(gradeRequest.generalPercent())
                .minPercent(gradeRequest.minPercent())
                .maxPercent(gradeRequest.maxPercent())
                .build();
        gradeRepository.save(grade);
    }

    @Override
    public Grade checkExistsGrade(Long gradeId) {
        return gradeRepository.findById(gradeId)
                .orElseThrow(() -> new ResourceNotFoundException("Grade", "id", gradeId));
    }

    @Override
    public void calculateGrade(GradeCalculationRequest gradeCalculationRequest) {
        StopWatch watch = new StopWatch();
        watch.start();

        //check active grade of market
        Optional<MarketGradeHistory> optional = marketGradeHistoryRepository
                .findByMarketIdAndIsActive(gradeCalculationRequest.marketId(), true);

        //check for if active grade of market is not present,code not throws exception
        if (optional.isPresent()) {
            MarketGradeHistory marketGradeHistory = optional.get();

            //show that external method,because all types of grade utilized it
            BigDecimal totalSale = calculateSalesOfMarket(marketGradeHistory.getMarket().getId(),
                    marketGradeHistory.getStartDate());

            //todo compare total sale with 0,if equal to zero throw exception

            Long gradeId = marketGradeHistory.getGrade().getId();

            Long marketId = marketGradeHistory.getMarket().getId();

            //select bonus amount of active positions
            List<GradeRuleResponse> gradeRules = gradeRuleRepository
                    .findByGradeIdAndMarketId(gradeId,
                            marketId);

            List<Long> employeeIds = gradeRules.stream()
                    .map(GradeRuleResponse::getEmployeeId)
                    .distinct()
                    .toList();

            List<EmployerContract> contracts = employerContractRepository
                    .findAllByEmployerIdInAndIsActive(employeeIds, true);

            Map<Long, EmployerContract> contractMap = contracts.stream()
                    .collect(Collectors.toMap(c -> c.getEmployer().getId(), Function.identity()));

            //create list for set all employers bonus amount with base salary
            List<EmployerContractResponse> employerContractResponse = new ArrayList<>();

            List<Long> employerIds = employerContractResponse.stream()
                    .map(EmployerContractResponse::getEmployerId)
                    .distinct()
                    .toList();

            Map<Long, Employer> employerMap = employerRepository.findAllById(employerIds)
                    .stream()
                    .collect(Collectors.toMap(Employer::getId, e -> e));

            List<GradeHistory> gradeHistories = new ArrayList<>();

            if (marketGradeHistory.getGrade().getGradeType() == GradeType.Fixed) {

                if (totalSale.compareTo(marketGradeHistory.getMinThreshold()) <= 0) {
                    throw new TargetNotReachedException("Market", "min threshold",
                            marketGradeHistory.getMinThreshold());
                }

                for (GradeRuleResponse gradeRuleResponse : gradeRules) {
                    EmployerContract contract = contractMap.get(gradeRuleResponse.getEmployeeId());

                    if (contract != null) {
                        EmployerContractResponse response = new EmployerContractResponse();

                        response.setGradeId(gradeRuleResponse.getGradeId());
                        response.setPositionId(contract.getPosition().getId());
                        response.setMarketId(contract.getMarket().getId());
                        response.setEmployerId(contract.getEmployer().getId());
                        response.setBaseSalary(contract.getBaseSalary());
                        response.setBonusAmount(gradeRuleResponse.getBonusAmount());
                        response.setCurrency(contract.getCurrency());
                        response.setValidFrom(contract.getValidFrom());
                        response.setValidTo(contract.getValidTo());

                        BigDecimal baseSalary = Optional.ofNullable(contract.getBaseSalary()).orElse(BigDecimal.ZERO);
                        BigDecimal bonusAmount = Optional.ofNullable(gradeRuleResponse.getBonusAmount()).orElse(BigDecimal.ZERO);
                        response.setTotalAmount(baseSalary.add(bonusAmount));

                        employerContractResponse.add(response);
                    }
                }

                //extract method,because all method used this.
                saveGradeHistories(employerContractResponse, employerMap);
            }

            if (marketGradeHistory.getGrade().getGradeType() == GradeType.Percent) {
                if (totalSale.compareTo(marketGradeHistory.getMinThreshold()) <= 0) {
                    throw new TargetNotReachedException("Market", "threshold", marketGradeHistory.getMinThreshold());
                }
                int countOfSpecialEmployer = gradeRules.size();

                int totalCountOfEmployer = employerContractRepository.countByMarketIdAndIsActive(marketId, true);

                int countOfNotSpecialEmployer = totalCountOfEmployer - countOfSpecialEmployer;

                BigDecimal fixAmount = totalSale.multiply(
                        marketGradeHistory.getGrade().getGeneralPercent().divide(BigDecimal.valueOf(100),
                                4, RoundingMode.HALF_UP));

                BigDecimal sumOfPercent = BigDecimal.ZERO;

                List<GradeRuleResponse> updatedRules = new ArrayList<>();

                for (GradeRuleResponse rule : gradeRules) {
                    BigDecimal bonusPercent = rule.getBonusPercent();
                    sumOfPercent = sumOfPercent.add(bonusPercent);
                    Long employeeId = rule.getEmployeeId();
                    // formula: (fixAmount * bonusPercent) / 100
                    BigDecimal newPriceOfGradeRule = fixAmount.multiply(bonusPercent)
                            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

                    updatedRules.add(new GradeRuleResponse(
                            rule.getGradeId(),
                            rule.getPositionId(),
                            employeeId,
                            rule.getMarketId(),
                            rule.getBonusPercent(),
                            newPriceOfGradeRule,
                            rule.getCurrency()
                    ));
                }

                BigDecimal newPercent = BigDecimal.valueOf(100).subtract(sumOfPercent);

                BigDecimal newPriceOfNotSpecialEmployer = fixAmount.multiply(
                        newPercent.divide(BigDecimal.valueOf(100),
                                4, RoundingMode.HALF_UP));

                BigDecimal amountPerEmployer = BigDecimal.ZERO;

                if (countOfNotSpecialEmployer > 0) {
                    amountPerEmployer = newPriceOfNotSpecialEmployer.divide(
                            BigDecimal.valueOf(countOfNotSpecialEmployer),
                            2,
                            RoundingMode.HALF_UP
                    );
                }
                for (GradeRuleResponse gradeRuleResponse : updatedRules) {
                    EmployerContract contract = contractMap.get(gradeRuleResponse.getEmployeeId());
                    if (contract != null) {
                        EmployerContractResponse response = new EmployerContractResponse();

                        response.setGradeId(gradeRuleResponse.getGradeId());
                        response.setPositionId(contract.getPosition().getId());
                        response.setMarketId(contract.getMarket().getId());
                        response.setEmployerId(contract.getEmployer().getId());
                        response.setBaseSalary(contract.getBaseSalary());
                        response.setBonusAmount(gradeRuleResponse.getBonusAmount());
                        response.setCurrency(contract.getCurrency());
                        response.setValidFrom(contract.getValidFrom());
                        response.setValidTo(contract.getValidTo());

                        BigDecimal baseSalary = Optional.ofNullable(contract.getBaseSalary()).orElse(BigDecimal.ZERO);
                        BigDecimal bonusAmount = Optional.ofNullable(gradeRuleResponse.getBonusAmount()).orElse(BigDecimal.ZERO);
                        response.setTotalAmount(baseSalary.add(bonusAmount));

                        employerContractResponse.add(response);
                    }
                }

                List<EmployerContractResponse> employerContracts = employerContractRepository
                        .findByEmployerIdNotIn(employeeIds, marketId, true);

                List<EmployerContractResponse> notSpecificEmployer = new ArrayList<>();

                for (EmployerContractResponse employerContractResponse1 : employerContracts) {
                    EmployerContractResponse response = new EmployerContractResponse();

                    response.setGradeId(employerContractResponse1.getGradeId());
                    response.setPositionId(employerContractResponse1.getPositionId());
                    response.setMarketId(employerContractResponse1.getMarketId());
                    response.setEmployerId(employerContractResponse1.getEmployerId());
                    response.setBaseSalary(employerContractResponse1.getBaseSalary());

                    response.setBonusAmount(amountPerEmployer);

                    response.setCurrency(employerContractResponse1.getCurrency());
                    response.setValidFrom(employerContractResponse1.getValidFrom());
                    response.setValidTo(employerContractResponse1.getValidTo());

                    response.setTotalAmount(amountPerEmployer.add(employerContractResponse1.getBaseSalary()));

                    notSpecificEmployer.add(response);
                }

                employerContractResponse.addAll(notSpecificEmployer);

                //extract method,because all method used this.
                saveGradeHistories(employerContractResponse, employerMap);
            }
            if (marketGradeHistory.getGrade().getGradeType() == GradeType.Threshold) {
                log.info("threshold olan if bloku basladi");
                List<EmployerContractResponse> employerContractResponses = new ArrayList<>();

                log.info("1ci EmployerContractResponse : {}", employerContractResponses);


                log.info("totalSale : {}", totalSale);

                int totalCountOfEmployer = employerContractRepository
                        .countByMarketIdAndIsActive(marketId, true);

                log.info("totalCountOfEmployer : {}", totalCountOfEmployer);

                BigDecimal middleThreshold = thresholdsUtil.calculateMiddleThreshold(marketGradeHistory.getMinThreshold(),
                        marketGradeHistory.getMaxThreshold());
                log.info("middleThreshold : {}", middleThreshold);

                // todo:extract method
                BigDecimal percent = BigDecimal.ZERO;
                if (totalSale.compareTo(marketGradeHistory.getMinThreshold()) >= 0 && totalSale.compareTo(middleThreshold) <= 0) {
                    percent = marketGradeHistory.getGrade().getMinPercent();
                } else if (totalSale.compareTo(middleThreshold) > 0 && totalSale.compareTo(marketGradeHistory.getMaxThreshold()) <= 0) {
                    percent = middleThreshold;
                } else if (totalSale.compareTo(marketGradeHistory.getMaxThreshold()) > 0) {
                    percent = marketGradeHistory.getGrade().getMaxPercent();
                }
                log.info("percent : {}", percent);

                BigDecimal fixAmount = totalSale.multiply(
                        percent.divide(BigDecimal.valueOf(100),
                                4, RoundingMode.HALF_UP));

                log.info("fixAmount : {}", fixAmount);


                BigDecimal bonusAmountPerEmployer = fixAmount.divide(
                        BigDecimal.valueOf(totalCountOfEmployer),
                        2,
                        RoundingMode.HALF_UP
                );

                log.info("amountPerEmployer : {}", bonusAmountPerEmployer);


                List<EmployerContractResponse> employerContracts = employerContractRepository
                        .findAllByMarketIdAndIsActive(marketId, true);

                log.info(" before employerContracts : {}", employerContracts);

                for (EmployerContractResponse updateContractResponse : employerContracts) {
                    BigDecimal baseSalary = Optional.ofNullable(updateContractResponse.getBaseSalary()).orElse(BigDecimal.ZERO);

                    updateContractResponse.setBonusAmount(bonusAmountPerEmployer);

                    updateContractResponse.setTotalAmount(baseSalary.add(bonusAmountPerEmployer));

                    employerContractResponses.add(updateContractResponse);
                }


                List<Long> list = employerContractResponses.stream()
                        .map(EmployerContractResponse::getEmployerId)
                        .distinct()
                        .toList();

                log.info("after employer contracts response : {}", employerContractResponses);

                log.info("list : {}", list);


                Map<Long, Employer> employerMap1 = employerRepository.findAllById(list)
                        .stream()
                        .collect(Collectors.toMap(Employer::getId, e -> e));

                log.info("employerMap 1 :{}", employerMap1);


                saveGradeHistories(employerContractResponses, employerMap1);

                //methodlara bol kodu seliqeye sal.artiq melumatlari sil.duzgun log yaz.

            }

        }
        watch.stop();
        log.info("Calculate Grade method execution time: {} ms", watch.getTotalTimeMillis());
    }


    public BigDecimal calculateSalesOfMarket(Long marketId, LocalDate startDate) {
        return saleRepository.sumPriceByMarketIdAndDate(marketId,
                startDate);
    }

    public void saveGradeHistories(List<EmployerContractResponse> responses, Map<Long, Employer> employerMap) {
        List<GradeHistory> histories = responses.stream().map(res -> {
            GradeHistory history = new GradeHistory();
            history.setEmployer(employerMap.get(res.getEmployerId()));
            history.setBaseSalary(res.getBaseSalary());
            history.setBonusAmount(res.getBonusAmount());
            history.setTotalSalary(res.getTotalAmount());
            history.setPaidAt(LocalDateTime.now());
            history.setPeriod("MONTHLY");

            log.info("histories after : {}",history);


            return history;


        }).toList();


        gradeHistoryRepository.saveAll(histories);

//        log.info("history : {}", histories);
    }
}