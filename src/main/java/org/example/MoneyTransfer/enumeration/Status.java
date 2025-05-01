package org.example.MoneyTransfer.enumeration;

public enum Status {
    SUCCESSFUL("Transfer completed successfully"),
    FAILED("Transfer failed"),
    INSUFFICIENT_FUNDS("Not enough balance to complete transfer"),
    INVALID_ACCOUNT("Account number is incorrect");

    private final String defaultMessage;

    Status(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
