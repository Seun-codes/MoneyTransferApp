package org.example.MoneyTransfer.controller;

import org.example.MoneyTransfer.Exception.InvalidTransactionAmountException;
import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.dto.TransactionSumary;
import org.example.MoneyTransfer.dto.TransferResponse;
import org.example.MoneyTransfer.enumeration.Status;
import org.example.MoneyTransfer.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.example.MoneyTransfer.dto.TransferRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestBody TransferRequest request)  {
        return transactionService.transfer(request);
    }
    @GetMapping("/transactions")
    public List<Transaction> getTransactions(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return transactionService.getTransactions(status, accountNumber, startDate, endDate);
    }



    @GetMapping("/summary")
    public TransactionSumary getSummary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return transactionService.generateSummary(date);
    }

}
