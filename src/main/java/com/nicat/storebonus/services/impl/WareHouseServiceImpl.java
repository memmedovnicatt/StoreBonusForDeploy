package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.WareHouseRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.entities.Company;
import com.nicat.storebonus.entities.WareHouse;
import com.nicat.storebonus.repositories.WareHouseRepository;
import com.nicat.storebonus.services.CompanyService;
import com.nicat.storebonus.services.WareHouseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WareHouseServiceImpl implements WareHouseService {
    WareHouseRepository wareHouseRepository;
    CompanyService companyService;

    @Override
    public void create(WareHouseRequest wareHouseRequest) {
        Company company = companyService.checkCompanyExists(wareHouseRequest.companyId());

        WareHouse wareHouse = WareHouse.builder()
                .location(wareHouseRequest.location())
                .name(wareHouseRequest.name())
                .company(company)
                .build();

        wareHouseRepository.save(wareHouse);

        ApiResponse.<Void>builder()
                .data(null)
                .message(ResponseMessage.SUCCESS_CREATE.getMessage())
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }
}