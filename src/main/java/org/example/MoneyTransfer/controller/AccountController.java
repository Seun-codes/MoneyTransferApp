package org.example.MoneyTransfer.controller;

import lombok.RequiredArgsConstructor;
import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.dto.CreateAccountRequest;
import org.example.MoneyTransfer.dto.DepositRequest;
import org.example.MoneyTransfer.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        return ResponseEntity.ok(accountService.createAccount(request));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Account> deposit(@RequestBody DepositRequest request) {
        return ResponseEntity.ok(accountService.deposit(request));
    }

}
