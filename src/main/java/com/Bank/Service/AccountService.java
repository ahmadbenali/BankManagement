package com.Bank.Service;

import com.Bank.Model.Account;
import com.Bank.Model.Transaction;
import com.Bank.Model.TransactionType;
import com.Bank.Model.User;
import com.Bank.Repository.AccountRepository;
import com.Bank.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Account Deposit(String  accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        //To log transaction audit trail
        //CASH not the same account number, because it comes from physical not from account
        //and for accurate
        Transaction tx = new Transaction(null, "CASH", accountNumber, amount, TransactionType.DEPOSIT, LocalDateTime.now());
        transactionRepository.save(tx);

        return account;
    }

    @Transactional
    public Account Withdraw(String  accountNumber, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        Account account=accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction(null, accountNumber, "CASH", amount, TransactionType.WITHDRAWAL, LocalDateTime.now());
        transactionRepository.save(tx);

        return account;
    }

    @Transactional
    public void Transfer(String  sourceAccountNumber,String destinationAccountNumber, BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");

        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account destAccount = accountRepository.findByAccountNumber(destinationAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds for transfer");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        accountRepository.save(sourceAccount);

        destAccount.setBalance(destAccount.getBalance().add(amount));
        accountRepository.save(destAccount);

        Transaction tx = new Transaction(
                null,
                sourceAccountNumber,
                destinationAccountNumber,
                amount,
                TransactionType.TRANSFER,
                LocalDateTime.now()
        );
        transactionRepository.save(tx);


    }
}
