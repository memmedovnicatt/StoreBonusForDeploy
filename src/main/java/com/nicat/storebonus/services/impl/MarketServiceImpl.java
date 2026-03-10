package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.MarketRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.entities.Grade;
import com.nicat.storebonus.entities.Market;
import com.nicat.storebonus.entities.MarketGradeHistory;
import com.nicat.storebonus.entities.WareHouse;
import com.nicat.storebonus.exceptions.handler.ResourceNotFoundException;
import com.nicat.storebonus.repositories.MarketGradeHistoryRepository;
import com.nicat.storebonus.repositories.MarketRepository;
import com.nicat.storebonus.services.GradeService;
import com.nicat.storebonus.services.MarketService;
import com.nicat.storebonus.services.WareHouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MarketServiceImpl implements MarketService {

    MarketRepository marketRepository;
    WareHouseService wareHouseService;
    GradeService gradeService;
    MarketGradeHistoryRepository marketGradeHistoryRepository;


    //transactional
    @Override
    public void create(MarketRequest marketRequest) {
        WareHouse wareHouse = wareHouseService.checkExistsWareHouse(marketRequest.wareHouseId());

        Grade grade = null;
        if (marketRequest.gradeId() != null) {
            grade = gradeService.checkExistsGrade(marketRequest.gradeId());
        }

        Market market = Market.builder()
                .wareHouse(wareHouse)
                .name(marketRequest.name())
                .location(marketRequest.location())
                .build();
        marketRepository.save(market);

        MarketGradeHistory marketGradeHistory = MarketGradeHistory.builder()
                .market(market)
                .grade(grade)
                .startDate(LocalDate.now())
                .minThreshold(marketRequest.minThreshold())
                .maxThreshold(marketRequest.maxThreshold())
                .build();
        marketGradeHistoryRepository.save(marketGradeHistory);

    }

    @Override
    public Market checkExistsMarket(Long marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new ResourceNotFoundException("Market", "id", marketId));
    }
}

