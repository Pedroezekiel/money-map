package com.projects.money_map_api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projects.money_map_api.entity.enums.TransactionType;
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
public class TransactionResponse {
    private String id;
    private String userId;
    private BigDecimal amount;
    private TransactionType  type;
    private String description;
    private String category;


}
