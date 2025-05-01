package org.example.MoneyTransfer.Exception;

public class InvalidTransactionRefException extends RuntimeException{
    public InvalidTransactionRefException(String message) {
        super(message);
    }
}
