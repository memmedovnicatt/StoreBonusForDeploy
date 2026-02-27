package com.nicat.storebonus.controllers;

import com.nicat.storebonus.dtos.request.PositionRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.PositionResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.services.PositionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PositionController {
    PositionService positionService;

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> createPosition(@Valid @RequestBody PositionRequest positionRequest) {
        positionService.create(positionRequest);
        return ResponseEntity.ok(
                ApiResponse.success(null, ResponseMessage.SUCCESS_CREATE)
        );
    }
}