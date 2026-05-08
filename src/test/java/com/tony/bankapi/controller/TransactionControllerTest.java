package com.tony.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tony.bankapi.dto.TransferRequest;
import com.tony.bankapi.dto.TransferResponse;
import com.tony.bankapi.service.JwtService;
import com.tony.bankapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @TestConfiguration
    static class TransactionServiceTestConfig {
        @Bean
        TransactionService transactionService() {
            return mock(TransactionService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldTransferSuccessfully() throws Exception {
        // Arrange
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("123");
        request.setDestinationAccountNumber("456");
        request.setAmount(new BigDecimal("200"));

        TransferResponse mockResponse = TransferResponse.builder()
                .message("Transfer successful")
                .sourceAccount("123")
                .destinationAccount("456")
                .amount(new BigDecimal("200"))
                .timestamp(LocalDateTime.now())
                .build();

        when(transactionService.transfer(
                any(String.class),
                any(String.class),
                any(BigDecimal.class)
        )).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful"))
                .andExpect(jsonPath("$.sourceAccount").value("123"))
                .andExpect(jsonPath("$.destinationAccount").value("456"))
                .andExpect(jsonPath("$.amount").value(200));
    }
}