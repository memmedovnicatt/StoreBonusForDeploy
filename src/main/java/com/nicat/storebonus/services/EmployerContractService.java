package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface EmployerContractService {
    void createContract(@Valid EmployerContractRequest employerContractRequest);
}
