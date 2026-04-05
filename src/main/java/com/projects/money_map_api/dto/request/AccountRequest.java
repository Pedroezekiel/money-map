package com.projects.money_map_api.dto.request;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    private String name;
    private String currency;
    private BigDecimal savingBalance;
    private BigDecimal spendingBalance;
}
