package com.tony.bankapi.service;

import com.tony.bankapi.entity.Account;

public interface AccountService {
    Account createAccount(Long userId);
    Account getAccountByNumber(String accountNumber);
    Double getBalance(String accountNumber);
}
