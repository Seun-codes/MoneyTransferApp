package org.example.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import org.example.MoneyTransfer.Exception.InvalidAccountException;
import org.example.MoneyTransfer.Repository.AccountRepository;
import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.dto.CreateAccountRequest;
import org.example.MoneyTransfer.dto.DepositRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    @Override
    public Account createAccount(CreateAccountRequest request) {
        String accountNumber = generateAccountNumber();
        BigDecimal balance = request.getInitialDeposit() != null ? request.getInitialDeposit() : BigDecimal.ZERO;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountName(request.getAccountName());
        account.setBalance(balance);
        account.setCreatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Override
    public Account deposit(DepositRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new InvalidAccountException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.valueOf((long)(Math.random() * 9_000_000_000L + 1_000_000_000L));
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}
