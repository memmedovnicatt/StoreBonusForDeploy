package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.GradeCalculationRequest;
import com.nicat.storebonus.dtos.request.GradeRequest;
import com.nicat.storebonus.dtos.response.MarketGradeHistoryResponse;
import com.nicat.storebonus.entities.Grade;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GradeService {
    void create(@Valid GradeRequest gradeRequest);

    Grade checkExistsGrade(Long gradeId);

    void calculateGrade(@Valid GradeCalculationRequest gradeCalculationRequest);

    List<MarketGradeHistoryResponse> getAll();
}
