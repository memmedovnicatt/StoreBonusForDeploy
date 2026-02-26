package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.CompanyRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.entities.Company;
import com.nicat.storebonus.exceptions.handler.ResourceNotFoundException;
import com.nicat.storebonus.repositories.CompanyRepository;
import com.nicat.storebonus.services.CompanyService;
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
public class CompanyServiceImpl implements CompanyService {
    CompanyRepository companyRepository;


    @Override
    public void create(CompanyRequest companyRequest) {
        Company company = Company.builder()
                .location(companyRequest.location())
                .name(companyRequest.name())
                .build();

        companyRepository.save(company);

        ApiResponse.<Void>builder()
                .data(null)
                .message(ResponseMessage.SUCCESS_CREATE.getMessage())
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Override
    public Company checkCompanyExists(Long companyId) {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            throw new ResourceNotFoundException("Company", companyId);
        }
        return company;
    }
}