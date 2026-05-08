package com.tony.bankapi.dto;

import java.math.BigDecimal;

public class AccountResponse {
    private String accountNumber;
    private BigDecimal balance;

    public AccountResponse(String accountNumber, BigDecimal balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
