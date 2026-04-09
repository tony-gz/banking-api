package com.tony.bankapi.service;

public interface TransactionService {
    void transfer(String sourceAccountNumber,
                  String destinationAccountNumber,
                  Double amount);
}
