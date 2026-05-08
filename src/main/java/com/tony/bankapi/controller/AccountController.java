package com.tony.bankapi.controller;

import com.tony.bankapi.dto.AccountRequest;
import com.tony.bankapi.dto.AccountResponse;
import com.tony.bankapi.entity.Account;
import com.tony.bankapi.entity.User;
import com.tony.bankapi.exception.AccountNotFoundException;
import com.tony.bankapi.exception.UserNotFoundException;
import com.tony.bankapi.repository.AccountRepository;
import com.tony.bankapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setBalance(request.getInitialBalance());
        account.setUser(user);

        Account saved = accountRepository.save(account);

        AccountResponse response = new AccountResponse(saved.getAccountNumber(), saved.getBalance());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountResponse> getBalance(@PathVariable String accountNumber){

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        AccountResponse response =
                new AccountResponse(account.getAccountNumber(), account.getBalance());

        return ResponseEntity.ok(response);
    }

}
