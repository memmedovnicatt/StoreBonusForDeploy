package com.nicat.storebonus.mapper;


import com.nicat.storebonus.dtos.request.EmployerContractRequest;
import com.nicat.storebonus.entities.EmployerContract;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployerContractMapper {
    EmployerContract toEmployerContract(EmployerContractRequest employerContractRequest);
}
