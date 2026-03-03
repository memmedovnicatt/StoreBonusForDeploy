package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.MarketRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface MarketService {
    void create(@Valid MarketRequest marketRequest);
}
