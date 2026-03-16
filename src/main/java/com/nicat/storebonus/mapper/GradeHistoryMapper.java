package com.nicat.storebonus.mapper;

import com.nicat.storebonus.dtos.response.MarketGradeHistoryResponse;
import com.nicat.storebonus.entities.GradeHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface GradeHistoryMapper {

    @Mapping(source = "employer.name", target = "employerName")
    @Mapping(source = "employer.surname", target = "employerSurname")
    @Mapping(source = "employer.phoneNumber", target = "phoneNumber")
    @Mapping(source = "employer.position.name", target = "positionName")
    @Mapping(source = "market.name", target = "marketName")
    MarketGradeHistoryResponse toGradeHistory(GradeHistory gradeHistory);

    List<MarketGradeHistoryResponse> toListGradeHistory(List<GradeHistory> gradeHistories);
}
