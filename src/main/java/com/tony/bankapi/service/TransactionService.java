package com.tony.bankapi.service;

import com.tony.bankapi.dto.TransferResponse;
import java.math.BigDecimal;

public interface TransactionService {
    TransferResponse transfer(String sourceAccountNumber,
                  String destinationAccountNumber,
                  BigDecimal amount);
}
