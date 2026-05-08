package com.tony.bankapi.service;

import com.tony.bankapi.entity.Account;
import java.math.BigDecimal;

public interface AccountService {
    Account createAccount(Long userId);
    Account getAccountByNumber(String accountNumber);
    BigDecimal getBalance(String accountNumber);
}
