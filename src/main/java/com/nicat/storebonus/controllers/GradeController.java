package com.nicat.storebonus.controllers;

import com.nicat.storebonus.dtos.request.GradeCalculationRequest;
import com.nicat.storebonus.dtos.request.GradeRequest;
import com.nicat.storebonus.dtos.request.GradeRuleRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.MarketGradeHistoryResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.GradeRuleService;
import com.nicat.storebonus.services.GradeService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grades")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradeController {

    GradeService gradeService;
    GradeRuleService gradeRuleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGrade(@Valid @RequestBody GradeRequest gradeRequest) {
        gradeService.create(gradeRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }

    @PostMapping("/bonus-calculation")
    public ResponseEntity<ApiResponse<Void>> calculateGrade(@Valid @RequestBody GradeCalculationRequest gradeCalculationRequest) {
        gradeService.calculateGrade(gradeCalculationRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CALCULATED));
    }

    @PostMapping("/rules")
    public ResponseEntity<ApiResponse<Void>> createGradePositionBonus(@Valid @RequestBody GradeRuleRequest gradeRuleRequest) {
        gradeRuleService.create(gradeRuleRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }

    @GetMapping("/histories")
    public ResponseEntity<ApiResponse<List<MarketGradeHistoryResponse>>> getAll() {
        List<MarketGradeHistoryResponse> marketGradeHistoryResponse = gradeService.getAll();
        return ResponseEntity.ok(ApiResponse.success(marketGradeHistoryResponse, ResponseMessage.SUCCESS_FETCH));
    }
}