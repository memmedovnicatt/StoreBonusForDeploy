package com.nicat.storebonus.controllers;


import com.nicat.storebonus.dtos.request.GradeRuleRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.GradeRuleService;
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
@RequestMapping("/grade-rules")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GradeRuleController {
    GradeRuleService gradeRuleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createGradePositionBonus(@Valid @RequestBody GradeRuleRequest gradeRuleRequest) {
        gradeRuleService.create(gradeRuleRequest);
        return ResponseEntity.ok(ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE));
    }
}
