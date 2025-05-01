package org.example.MoneyTransfer.service;

import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.dto.CreateAccountRequest;
import org.example.MoneyTransfer.dto.DepositRequest;

public interface AccountService {
    Account createAccount(CreateAccountRequest request);
    Account deposit(DepositRequest request);
}
