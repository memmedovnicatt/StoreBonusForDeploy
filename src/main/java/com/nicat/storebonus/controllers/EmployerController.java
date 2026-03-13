package com.nicat.storebonus.controllers;

import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.dtos.request.EmployerRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.EmployerContractService;
import com.nicat.storebonus.services.EmployerService;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerController {
    EmployerService employerService;
    EmployerContractService employerContractService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createEmployer(@Valid @RequestBody EmployerRequest employerRequest) {
        employerService.create(employerRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }

    @PostMapping("/contract")
    public ResponseEntity<ApiResponse<Void>> createEmployerContract(@Valid @RequestBody EmployerContractRequest employerContractRequest) {
        employerContractService.createContract(employerContractRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }
}