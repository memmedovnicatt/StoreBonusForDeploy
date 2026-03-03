package com.nicat.storebonus.controllers;

import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.EmployerContractService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employer-contracts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerContractController {
    EmployerContractService employerContractService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createEmployerContract(@Valid @RequestBody EmployerContractRequest employerContractRequest) {
        employerContractService.create(employerContractRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }

}
