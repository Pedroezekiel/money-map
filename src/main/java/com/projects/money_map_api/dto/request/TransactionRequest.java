package com.projects.money_map_api.dto.request;

import com.projects.money_map_api.entity.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {



    @NotNull(message = "AmountId is required")
    private String accountId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Amount must have at most 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Transaction type is required")
    private TransactionType type;

    private String description;

    private String category;
}
