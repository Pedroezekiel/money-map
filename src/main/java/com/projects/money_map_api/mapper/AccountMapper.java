package com.projects.money_map_api.mapper;

import com.projects.money_map_api.dto.request.AccountRequest;
import com.projects.money_map_api.dto.response.AccountResponse;
import com.projects.money_map_api.entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {


    @Mapping(target = "name", source = "accountName")
    @Mapping(target = "userName",source = "user.firstName")
    AccountResponse toResponse(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account toEntity(AccountRequest accountRequest,  @MappingTarget Account account);
}