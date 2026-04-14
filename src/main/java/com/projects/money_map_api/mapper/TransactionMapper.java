package com.projects.money_map_api.mapper;

import com.projects.money_map_api.dto.response.TransactionResponse;
import com.projects.money_map_api.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction transaction);
}
