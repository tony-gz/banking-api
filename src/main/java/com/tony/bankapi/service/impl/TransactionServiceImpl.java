package com.tony.bankapi.service.impl;

import com.tony.bankapi.dto.TransferResponse;
import com.tony.bankapi.entity.Account;
import com.tony.bankapi.entity.Transaction;
import com.tony.bankapi.exception.AccountNotFoundException;
import com.tony.bankapi.exception.BadRequestException;
import com.tony.bankapi.exception.InsufficientFundsException;
import com.tony.bankapi.repository.AccountRepository;
import com.tony.bankapi.repository.TransactionRepository;
import com.tony.bankapi.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransferResponse transfer(String sourceAccountNumber,
                          String destinationAccountNumber,
                          BigDecimal amount) {

        log.info("Starting transfer from {} to {} amount {}", sourceAccountNumber, destinationAccountNumber, amount);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid amount: {}", amount);
            throw new BadRequestException("Amount must be greater than zero");
        }

        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            log.error("Attempt to transfer to same account: {}", sourceAccountNumber);
            throw new BadRequestException("Cannot transfer to the same account");
        }

        log.info("Validations passed for transfer");

        Account source = accountRepository.findByAccountNumberWithLock(sourceAccountNumber)
                .orElseThrow(() -> {
                    log.error("Source account not found: {}", sourceAccountNumber);
                    return new AccountNotFoundException("Source account not found");
                });

        Account destination = accountRepository.findByAccountNumberWithLock(destinationAccountNumber)
                .orElseThrow(() -> {
                    log.error("Destination account not found: {}", destinationAccountNumber);
                    return new AccountNotFoundException("Destination account not found");
                });

        if (source.getBalance().compareTo(amount) < 0){
            log.error("Insufficient funds for account {}: balance {}, amount {}", sourceAccountNumber, source.getBalance(), amount);
            throw new InsufficientFundsException("Insufficient funds");
        }

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        accountRepository.save(source);
        accountRepository.save(destination);

        Transaction tx = new Transaction();
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx.setType("TRANSFER");
        tx.setSourceAccount(source);
        tx.setDestinationAccount(destination);

        transactionRepository.save(tx);

        log.info("Transfer successful from {} to {} amount {}", sourceAccountNumber, destinationAccountNumber, amount);

        return TransferResponse.builder()
                .message("Transfer successful")
                .sourceAccount(sourceAccountNumber)
                .destinationAccount(destinationAccountNumber)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
