package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.SaleRequest;
import com.nicat.storebonus.dtos.response.FinalSalaryResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SaleService {
    void create(@Valid SaleRequest saleRequest);

    List<FinalSalaryResponse> calculateFinalSalary();
}
