package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.PositionRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface PositionService {
    void create(@Valid PositionRequest positionRequest);
}
