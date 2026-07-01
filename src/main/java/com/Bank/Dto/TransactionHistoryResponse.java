package com.Bank.Dto;

import com.Bank.Model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TransactionHistoryResponse {
    private String accountNumber;
    private BigDecimal currentBalance;
    private AccountType accountType;
    private List<TransactionDTO> transactions;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TransactionDTO {
        private Long id;
        private String sourceAccountNumber;
        private String destinationAccountNumber;
        private BigDecimal amount;
        private String transactionType;
        private String timestamp;
    }
}
