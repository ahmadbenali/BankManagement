package com.Bank.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sourceAccountNumber; // The account sending money (or "CASH" for deposits)

    @Column(nullable = false)
    private String destinationAccountNumber; // The account receiving money (or "CASH" for withdrawals)

    @NotNull(message = "Transaction amount cannot be null")
    @Positive(message = "Transaction amount must be greater than zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType; // DEPOSIT, WITHDRAWAL, or TRANSFER

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
