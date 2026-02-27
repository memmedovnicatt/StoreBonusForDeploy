package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.PositionRequest;
import com.nicat.storebonus.dtos.response.PositionResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PositionService {
    void create(@Valid PositionRequest positionRequest);

    List<PositionResponse> findAll();
}
