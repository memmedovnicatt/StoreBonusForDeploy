package com.nicat.storebonus.controllers;


import com.nicat.storebonus.dtos.request.CompanyRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.CompanyService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {

    CompanyService companyService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        companyService.create(companyRequest);
        return ResponseEntity.ok(
                ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE)
        );
    }
}
