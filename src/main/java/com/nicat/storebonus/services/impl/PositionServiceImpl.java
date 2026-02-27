package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.PositionRequest;
import com.nicat.storebonus.dtos.response.ApiResponse;
import com.nicat.storebonus.dtos.response.ResponseMessage;
import com.nicat.storebonus.entities.Position;
import com.nicat.storebonus.exceptions.handler.ResourceAlreadyExistsException;
import com.nicat.storebonus.mapper.PositionMapper;
import com.nicat.storebonus.repositories.PositionRepository;
import com.nicat.storebonus.services.PositionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PositionServiceImpl implements PositionService {

    PositionRepository positionRepository;
    PositionMapper positionMapper;

    @Override
    public void create(PositionRequest positionRequest) {
        boolean checkPositionName = positionRepository.existsByName(positionRequest.name());
        if (checkPositionName) {
            throw new ResourceAlreadyExistsException(positionRequest.name());
        }

        Position position = Position.builder()
                .name(positionRequest.name())
                .build();

        positionRepository.save(position);

        ApiResponse.<Void>builder()
                .data(null)
                .message(ResponseMessage.SUCCESS_CREATE.getMessage())
                .success(true)
                .timestamp(LocalDateTime.now())
                .build();
    }
}