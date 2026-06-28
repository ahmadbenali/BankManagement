package com.Bank.Dto;


import com.Bank.Model.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Initial balance cannot be null")
    @PositiveOrZero(message = "Initial balance cannot be negative")
    private BigDecimal initialBalance;

    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
