package com.nicat.storebonus.services.impl;

import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.dtos.request.EmployerRequest;
import com.nicat.storebonus.entities.Employer;
import com.nicat.storebonus.entities.Position;
import com.nicat.storebonus.exceptions.handler.ResourceNotFoundException;
import com.nicat.storebonus.mapper.EmployerMapper;
import com.nicat.storebonus.repositories.EmployerRepository;
import com.nicat.storebonus.repositories.PositionRepository;
import com.nicat.storebonus.services.EmployerService;
import com.nicat.storebonus.services.PositionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployerServiceImpl implements EmployerService {
    EmployerRepository employerRepository;
    PositionService positionService;
    EmployerMapper employerMapper;

    @Override
    public void create(EmployerRequest employerRequest) {
        Position position = positionService
                .checkExistsPosition(employerRequest.positionId());

        Employer savedEmployer = employerMapper.toEmployer(employerRequest);
        savedEmployer.setPosition(position);

        employerRepository.save(savedEmployer);
    }

    @Override
    public Employer checkExistsEmployer(Long employerId) {
        return employerRepository.findById(employerId)
                .orElseThrow(() -> new ResourceNotFoundException("Employer", "id", employerId));
    }
}