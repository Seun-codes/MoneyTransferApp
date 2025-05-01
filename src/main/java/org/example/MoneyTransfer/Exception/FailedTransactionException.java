package org.example.MoneyTransfer.Exception;

public class FailedTransactionException extends RuntimeException {
    public FailedTransactionException(String message) {
        super(message);
    }
}
