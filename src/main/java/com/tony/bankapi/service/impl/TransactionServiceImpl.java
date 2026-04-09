package com.tony.bankapi.service.impl;

import com.tony.bankapi.entity.Account;
import com.tony.bankapi.entity.Transaction;
import com.tony.bankapi.repository.AccountRepository;
import com.tony.bankapi.repository.TransactionRepository;
import com.tony.bankapi.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public void transfer(String sourceAccountNumber,
                         String destinationAccountNumber,
                         Double amount) {
    Account source = accountRepository.findByAccountNumberWithLock(sourceAccountNumber)
            .orElseThrow(() -> new RuntimeException("Source account not found"));

    Account destination = accountRepository.findByAccountNumberWithLock(destinationAccountNumber)
            .orElseThrow(() -> new RuntimeException("Destination account not found"));

    if (source.getBalance() < amount){
        throw new RuntimeException("Insufficient funds");
    }

    source.setBalance(source.getBalance() - amount);
    destination.setBalance(destination.getBalance() + amount);

    accountRepository.save(source);
    accountRepository.save(destination);

    Transaction tx = new Transaction();
    tx.setAmount(amount);
    tx.setTimestamp(LocalDateTime.now());
    tx.setType("TRANSFER");
    tx.setSourceAccount(source);
    tx.setDestinationAccount(destination);

    transactionRepository.save(tx);
    }
}
