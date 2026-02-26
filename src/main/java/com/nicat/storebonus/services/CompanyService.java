package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.CompanyRequest;
import com.nicat.storebonus.entities.Company;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

@Service
public interface CompanyService {
    void create(@Valid CompanyRequest companyRequest);

    Company checkCompanyExists(Long companyId);
}