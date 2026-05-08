package com.tony.bankapi.service;

import com.tony.bankapi.entity.Account;
import com.tony.bankapi.repository.AccountRepository;
import com.tony.bankapi.repository.TransactionRepository;
import com.tony.bankapi.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    void shouldTransferSuccessfully() {
        // 🔹 Arrange (datos de prueba)
        Account source = new Account();
        source.setAccountNumber("123");
        source.setBalance(new BigDecimal("1000"));

        Account destination = new Account();
        destination.setAccountNumber("456");
        destination.setBalance(new BigDecimal("500"));

        // 🔹 Mock comportamiento repositorio
        when(accountRepository.findByAccountNumberWithLock("123"))
                .thenReturn(Optional.of(source));

        when(accountRepository.findByAccountNumberWithLock("456"))
                .thenReturn(Optional.of(destination));

        // 🔹 Act
        transactionService.transfer("123", "456", new BigDecimal("200"));

        // 🔹 Assert (verificaciones)
        assertEquals(new BigDecimal("800"), source.getBalance());
        assertEquals(new BigDecimal("700"), destination.getBalance());

        verify(accountRepository).save(source);
        verify(accountRepository).save(destination);
        verify(transactionRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        Account source = new Account();
        source.setAccountNumber("123");
        source.setBalance(new BigDecimal("100"));

        Account destination = new Account();
        destination.setAccountNumber("456");
        destination.setBalance(new BigDecimal("500"));

        when(accountRepository.findByAccountNumberWithLock("123"))
                .thenReturn(Optional.of(source));

        when(accountRepository.findByAccountNumberWithLock("456"))
                .thenReturn(Optional.of(destination));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transfer("123", "456", new BigDecimal("200"));
        });

        assertEquals("Insufficient funds", exception.getMessage());
    }


    @Test
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        when(accountRepository.findByAccountNumberWithLock("123"))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService.transfer("123", "456", new BigDecimal("100"));
        });

        assertEquals("Source account not found", exception.getMessage());
    }

}