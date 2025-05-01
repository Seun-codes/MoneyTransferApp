package org.example.MoneyTransfer.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.MoneyTransfer.Repository.AccountRepository;
import org.example.MoneyTransfer.Repository.TransactionRepository;
import org.example.MoneyTransfer.Repository.UserRepository;
import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.dto.TransferRequest;
import org.example.MoneyTransfer.dto.TransferResponse;
import org.example.MoneyTransfer.enumeration.Status;
import org.example.MoneyTransfer.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;
    private String jwtToken;

    @BeforeEach
    void setUp() throws Exception {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        jwtToken = obtainAccessToken();
    }

    private String obtainAccessToken() throws Exception {
        String username = "testuser";
        String password = "testpass";
        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        String jsonPayload = objectMapper.writeValueAsString(payload);
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
        // Register user
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());

        // Login and extract token
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("token").asText();
    }

    @Test
    void transfer() throws Exception {

        Account account = new Account();
        account.setAccountName(" user A");
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("5000000"));
        accountRepository.save(account);

        TransferResponse response = new TransferResponse("123456789034",Status.SUCCESSFUL,"Transfer completed successfully",BigDecimal.valueOf(90600),"00");

        TransferRequest request = new TransferRequest("1234567890","3456789023",BigDecimal.valueOf(90500),"payment","123456789034");

        mockMvc.perform(post("/api/transfer")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SUCCESSFUL"))
                .andExpect(jsonPath("$.message").value("Transfer completed successfully"));
    }

    @Test
    void getTransactions() {
    }

    @Test
    void getSummary() {
    }
}