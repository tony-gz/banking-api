package com.tony.bankapi.service.impl;

import com.tony.bankapi.entity.Account;
import com.tony.bankapi.entity.User;
import com.tony.bankapi.exception.AccountNotFoundException;
import com.tony.bankapi.exception.UserNotFoundException;
import com.tony.bankapi.repository.AccountRepository;
import com.tony.bankapi.repository.UserRepository;
import com.tony.bankapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Account createAccount(Long userId){
        User user =userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setBalance(BigDecimal.ZERO);
        account.setUser(user);

        return accountRepository.save(account);
    }
    @Override
    public Account getAccountByNumber(String accountNumber){
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
    @Override
    public BigDecimal getBalance(String accountNumber){
        return getAccountByNumber(accountNumber).getBalance();
    }



}
