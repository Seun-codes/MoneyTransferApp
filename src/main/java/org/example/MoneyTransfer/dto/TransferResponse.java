package org.example.MoneyTransfer.dto;

import lombok.AllArgsConstructor;
import org.example.MoneyTransfer.enumeration.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
public class TransferResponse {
    private String transactionReference;
    private Status status;
    private String message;
    private BigDecimal billedAmount;
    private String responseCode;
}
