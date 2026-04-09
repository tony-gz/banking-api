package com.tony.bankapi.dto;

public class AccountResponse {
    private String accountNumber;
    private Double balance;

    public AccountResponse(String accountNumber, Double balance){
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
