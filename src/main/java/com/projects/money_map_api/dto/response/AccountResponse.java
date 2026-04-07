package com.projects.money_map_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private String id;
    private String name;
    private String description;
    private String category;
    private String currency;
    private BigDecimal savingBalance;
    private BigDecimal spendingBalance;
    private String userName;
}
