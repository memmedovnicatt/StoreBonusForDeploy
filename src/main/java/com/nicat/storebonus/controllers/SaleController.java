package com.nicat.storebonus.controllers;


import com.nicat.storebonus.dtos.request.SaleRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.FinalSalaryResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.SaleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SaleController {
    SaleService saleService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createSale(@Valid @RequestBody SaleRequest saleRequest) {
        saleService.create(saleRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }

    @PostMapping("/final-salary")
    public ResponseEntity<ApiResponse<List<FinalSalaryResponse>>> calculateFinalSalary() {
        List<FinalSalaryResponse> list = saleService.calculateFinalSalary();
        return ResponseEntity.ok(ApiResponse.success(list, ResponseMessage.SUCCESS_FETCH)); //change response message
    }
}
