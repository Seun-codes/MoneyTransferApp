package org.example.MoneyTransfer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.MoneyTransfer.Repository.AccountRepository;
import org.example.MoneyTransfer.Repository.TransactionRepository;
import org.example.MoneyTransfer.Repository.UserRepository;
import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.dto.CreateAccountRequest;
import org.example.MoneyTransfer.dto.DepositRequest;
import org.example.MoneyTransfer.dto.TransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
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
    void createAccount() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setAccountName("Test User");
        request.setInitialDeposit(new BigDecimal("1000"));

        mockMvc.perform(post("/api/accounts/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertThat(accountRepository.findAll()).hasSize(1);

    }

    @Test
    void testDeposit() throws Exception {
        Account account = new Account();
        account.setAccountName(" user A");
        account.setAccountNumber("1234567890");
        account.setBalance(new BigDecimal("100"));
        accountRepository.save(account);

        DepositRequest request = new DepositRequest();
        request.setAccountNumber("1234567890");
        request.setAmount(new BigDecimal("50"));

        mockMvc.perform(post("/api/accounts/deposit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.00));

        Account updated = accountRepository.findByAccountNumber("1234567890").orElseThrow();
        assertThat(updated.getBalance()).isEqualByComparingTo(String.valueOf(150));


    }

}