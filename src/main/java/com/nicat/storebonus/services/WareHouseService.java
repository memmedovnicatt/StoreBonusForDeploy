package com.nicat.storebonus.services;

import com.nicat.storebonus.dtos.request.WareHouseRequest;
import jakarta.validation.Valid;

public interface WareHouseService {
    void create(@Valid WareHouseRequest wareHouseRequest);
}
