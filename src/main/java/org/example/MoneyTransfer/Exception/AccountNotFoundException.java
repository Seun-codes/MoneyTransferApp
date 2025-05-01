package org.example.MoneyTransfer.Exception;

public class AccountNotFoundException extends RuntimeException
{
    public AccountNotFoundException(String message) {
        super(message);
    }
}
