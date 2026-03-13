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
        log.info("calculateGrade method was started with marketId: {}", gradeCalculationRequest.marketId());

        Optional<MarketGradeHistory> optional =
                checkActiveGradeOfMarket(gradeCalculationRequest.marketId(), true);

        //check for if active grade of market is not present,code not throws exception
        if (optional.isPresent()) {
            MarketGradeHistory marketGradeHistory = optional.get();

            //show that external method,because all types of grade utilized it
            BigDecimal totalSale = calculateSalesOfMarket(marketGradeHistory.getMarket().getId(),
                    marketGradeHistory.getStartDate());

            //check total sales of market,if conditions are true,the code will not be executed
            //todo:extract general method
            if (totalSale.compareTo(marketGradeHistory.getMinThreshold()) <= 0) {
                throw new TargetNotReachedException("Market", "min threshold",
                        marketGradeHistory.getMinThreshold());
            }

            //todo:extract general method
            GradeType gradeType = marketGradeHistory.getGrade().getGradeType();
            switch (gradeType) {
                case Fixed -> handleFixedGrade(marketGradeHistory);
                case Threshold -> handleThresholdGrade(marketGradeHistory, totalSale);
                case Percent -> handlePercentGrade(marketGradeHistory, totalSale);
            }

        } else {
            throw new ResourceNotFoundException("Grade of market", "id", gradeCalculationRequest.marketId());
        }
        watch.stop();
        log.info("Calculate Grade method execution time: {} ms", watch.getTotalTimeMillis());
    }

    private void handlePercentGrade(MarketGradeHistory marketGradeHistory, BigDecimal totalSale) {
        //select bonus amount of active positions
        //todo:maybe extract general method - > getRules()
        List<GradeRuleResponse> rules = gradeRuleRepository
                .findByGradeIdAndMarketId(marketGradeHistory.getGrade().getId(),
                        marketGradeHistory.getMarket().getId());

        List<Long> employeeIds = collectEmployeeIds(rules);

        Long marketId = marketGradeHistory.getMarket().getId();

        int totalStaff = employerContractRepository
                .countByMarketIdAndIsActive(marketGradeHistory.getMarket().getId(), true);

        BigDecimal fixAmount = totalSale.multiply(
                marketGradeHistory.getGrade().getGeneralPercent().divide(BigDecimal.valueOf(100),
                        4, RoundingMode.HALF_UP));

        List<EmployerContractResponse> specialResponses = calculateSpecialPercentBonus(rules, fixAmount, getContractMap(employeeIds));

        BigDecimal distributedPercent = rules.stream()
                .map(GradeRuleResponse::getBonusPercent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<EmployerContractResponse> nonSpecialResponses = calculateNonSpecialPercentBonus(marketGradeHistory, fixAmount, distributedPercent, employeeIds, totalStaff);

        List<EmployerContractResponse> allResponses = new ArrayList<>(specialResponses);
        allResponses.addAll(nonSpecialResponses);

        saveGradeHistories(allResponses, getEmployerMap(allResponses.stream().map(EmployerContractResponse::getEmployerId).toList()));
    }

    private Map<Long, Employer> getEmployerMap(List<Long> ids) {
        return employerRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Employer::getId, e -> e));
    }


    private Map<Long, EmployerContract> getContractMap(List<Long> ids) {
        return employerContractRepository.findAllByEmployerIdInAndIsActive(ids, true).stream()
                .collect(Collectors.toMap(c -> c.getEmployer().getId(), Function.identity()));
    }


    private List<EmployerContractResponse> calculateSpecialPercentBonus(List<GradeRuleResponse> rules, BigDecimal pool, Map<Long, EmployerContract> contractMap) {
        return rules.stream().map(rule -> {
            BigDecimal individualBonus = pool.multiply(rule.getBonusPercent()).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            return mapToResponse(contractMap.get(rule.getEmployeeId()), individualBonus, rule.getGradeId());
        }).toList();
    }

    private List<EmployerContractResponse> calculateNonSpecialPercentBonus(MarketGradeHistory history, BigDecimal pool, BigDecimal usedPercent, List<Long> excludedIds, int totalStaff) {
        int remainingCount = totalStaff - excludedIds.size();
        if (remainingCount <= 0) return Collections.emptyList();

        BigDecimal remainingPercent = BigDecimal.valueOf(100).subtract(usedPercent);
        BigDecimal remainingPool = pool.multiply(remainingPercent.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
        BigDecimal amountPerPerson = remainingPool.divide(BigDecimal.valueOf(remainingCount), 2, RoundingMode.HALF_UP);

        return employerContractRepository.findByEmployerIdNotIn(excludedIds, history.getMarket().getId(), true)
                .stream()
                .peek(res -> {
                    res.setBonusAmount(amountPerPerson);
                    res.setTotalAmount(res.getBaseSalary().add(amountPerPerson));
                }).toList();
    }


    private void handleFixedGrade(MarketGradeHistory marketGradeHistory) {
        //select bonus amount of active positions
        //todo:maybe extract general method - > getRules()
        List<GradeRuleResponse> rules = gradeRuleRepository
                .findByGradeIdAndMarketId(marketGradeHistory.getGrade().getId(),
                        marketGradeHistory.getMarket().getId());

        //select employeeIds from rules
        List<Long> employeeIds = collectEmployeeIds(rules);

        //todo:maybe extract general method - > getContractMap()
        Map<Long, EmployerContract> contractMap = employerContractRepository
                .findAllByEmployerIdInAndIsActive(employeeIds, true).stream()
                .collect(Collectors.toMap(c -> c.getEmployer().getId(), Function.identity()));

        //todo:maybe extract general method - > getEmployerMap()
        Map<Long, Employer> employerMap = employerRepository.findAllById(employeeIds)
                .stream()
                .collect(Collectors.toMap(Employer::getId, e -> e));

        List<EmployerContractResponse> employerContractResponses = rules.stream()
                .map(rule -> mapToResponse(contractMap.get(rule.getEmployeeId()), rule.getBonusAmount(), rule.getGradeId()))
                .toList();

        //extract method,because all methods used this.
        saveGradeHistories(employerContractResponses, employerMap);
    }

    private void handleThresholdGrade(MarketGradeHistory marketGradeHistory, BigDecimal totalSale) {
        log.info("handleThresholdGrade method was started");
        List<EmployerContractResponse> employerContractResponses = new ArrayList<>();

        int totalCountOfEmployer = employerContractRepository
                .countByMarketIdAndIsActive(marketGradeHistory.getMarket().getId(), true);

        //calculate middle threshold
        BigDecimal middleThreshold = calculateMiddleThreshold(marketGradeHistory.getMinThreshold(),
                marketGradeHistory.getMaxThreshold());

        //calculate percent
        BigDecimal percent = calculateThresholdPercent(totalSale,
                marketGradeHistory, middleThreshold);

        //calculate new amount with new percent
        BigDecimal fixAmount = totalSale.multiply(
                percent.divide(BigDecimal.valueOf(100),
                        4, RoundingMode.HALF_UP));

        //calculate bonus amount for each employee
        BigDecimal bonusAmountPerEmployee = fixAmount.divide(
                BigDecimal.valueOf(totalCountOfEmployer),
                2,
                RoundingMode.HALF_UP
        );

        List<EmployerContractResponse> employerContracts = employerContractRepository
                .findAllByMarketIdAndIsActive(marketGradeHistory.getMarket().getId(), true);

        //todo:maybe change to stream api format line 163-182,but my opinion this format more readable
        for (EmployerContractResponse updateContractResponse : employerContracts) {
            BigDecimal baseSalary = Optional.ofNullable(updateContractResponse.getBaseSalary()).orElse(BigDecimal.ZERO);

            updateContractResponse.setBonusAmount(bonusAmountPerEmployee);

            updateContractResponse.setTotalAmount(baseSalary.add(bonusAmountPerEmployee));

            employerContractResponses.add(updateContractResponse);
        }

        List<Long> employeeIds = employerContractResponses.stream()
                .map(EmployerContractResponse::getEmployerId)
                .distinct()
                .toList();

        Map<Long, Employer> employerMap = employerRepository.findAllById(employeeIds)
                .stream()
                .collect(Collectors.toMap(Employer::getId, e -> e));

        saveGradeHistories(employerContractResponses, employerMap);
    }

    public EmployerContractResponse mapToResponse(EmployerContract contract, BigDecimal bonus, Long gradeId) {
        BigDecimal baseSalary = Optional.ofNullable(contract.getBaseSalary())
                .orElse(BigDecimal.ZERO);
        return EmployerContractResponse.builder()
                .totalAmount(baseSalary.add(bonus))
                .baseSalary(baseSalary)
                .employerId(contract.getEmployer().getId())
                .gradeId(gradeId)
                .positionId(contract.getPosition().getId())
                .marketId(contract.getMarket().getId())
                .bonusAmount(bonus)
                .currency(contract.getCurrency())
                .validFrom(contract.getValidFrom())
                .validTo(contract.getValidTo())
                .build();
    }

    private BigDecimal calculateThresholdPercent(BigDecimal totalSale, MarketGradeHistory marketGradeHistory, BigDecimal middleThreshold) {
        log.info("calculateThresholdPercent method was started"); //todo: AOP logging
        BigDecimal percent = BigDecimal.ZERO;
        if (totalSale.compareTo(marketGradeHistory.getMinThreshold()) >= 0 && totalSale.compareTo(middleThreshold) <= 0) {
            percent = marketGradeHistory.getGrade().getMinPercent();
        } else if (totalSale.compareTo(middleThreshold) > 0 && totalSale.compareTo(marketGradeHistory.getMaxThreshold()) <= 0) {
            percent = middleThreshold;
        } else if (totalSale.compareTo(marketGradeHistory.getMaxThreshold()) > 0) {
            percent = marketGradeHistory.getGrade().getMaxPercent();
        }
        return percent;

    }

    public BigDecimal calculateMiddleThreshold(BigDecimal a, BigDecimal b) {
        return a.add(b).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
    }

    public List<Long> collectEmployeeIds(List<GradeRuleResponse> gradeRules) {
        return gradeRules.stream()
                .map(GradeRuleResponse::getEmployeeId)
                .distinct()
                .toList();
    }

    private BigDecimal calculateSalesOfMarket(Long marketId, LocalDate startDate) {
        return saleRepository.sumPriceByMarketIdAndDate(marketId,
                startDate);
    }

    private void saveGradeHistories(List<EmployerContractResponse> responses, Map<Long, Employer> employerMap) {
        List<GradeHistory> histories = responses.stream().map(res -> {
            GradeHistory history = new GradeHistory();
            history.setEmployer(employerMap.get(res.getEmployerId()));
            history.setBaseSalary(res.getBaseSalary());
            history.setBonusAmount(res.getBonusAmount());
            history.setTotalSalary(res.getTotalAmount());
            history.setPaidAt(LocalDateTime.now());
            history.setPeriod("MONTHLY");
            return history;


        }).toList();
        gradeHistoryRepository.saveAll(histories);
    }

    public Optional<MarketGradeHistory> checkActiveGradeOfMarket(Long marketId, boolean isActive) {
        return marketGradeHistoryRepository.findByMarketIdAndIsActive(marketId, isActive);
    }
}