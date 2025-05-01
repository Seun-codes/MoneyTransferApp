package org.example.MoneyTransfer.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest {

    private String accountName;
    private BigDecimal initialDeposit;
}
