package com.Bank.Controller;


import com.Bank.Dto.AccountRequest;
import com.Bank.Dto.TransactionRequest;
import com.Bank.Model.Account;
import com.Bank.Model.User;
import com.Bank.Repository.AccountRepository;
import com.Bank.Service.AccountService;
import com.Bank.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }


    // open a new account
    @PostMapping
    public ResponseEntity<?> openAccount(@Valid @RequestBody AccountRequest request) {
        try {
            Account savedAccount = accountService.registerAccount(
                    request.getUserId(),
                    request.getInitialBalance(),
                    request.getAccountType()
            );
            return new ResponseEntity<>("Account opened successfully! Number: " + savedAccount.getAccountNumber(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // account details
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccountDetails(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByNumber(accountNumber);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Valid @RequestBody TransactionRequest request) {
        try {
            Account updatedAccount = accountService.Deposit(request.getDestinationAccountNumber(), request.getAmount());
            return new ResponseEntity<>("Deposit successful. New Balance: $" + updatedAccount.getBalance(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Valid @RequestBody TransactionRequest request) {
        try {
            Account updatedAccount = accountService.Withdraw(request.getDestinationAccountNumber(), request.getAmount());
            return new ResponseEntity<>("Withdrawal successful. New Balance: $" + updatedAccount.getBalance(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransactionRequest request) {
        try {
            if (request.getSourceAccountNumber() == null || request.getSourceAccountNumber().isBlank()) {
                return new ResponseEntity<>("Source account number is required for transfers", HttpStatus.BAD_REQUEST);
            }

            accountService.Transfer(
                    request.getSourceAccountNumber(),
                    request.getDestinationAccountNumber(),
                    request.getAmount()
            );
            return new ResponseEntity<>("Transfer completed successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
