package org.example.MoneyTransfer.service;

import org.example.MoneyTransfer.Exception.InvalidTransactionAmountException;
import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.dto.TransactionSumary;
import org.example.MoneyTransfer.dto.TransferResponse;
import org.example.MoneyTransfer.dto.TransferRequest;
import org.example.MoneyTransfer.enumeration.Status;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    TransferResponse transfer(TransferRequest request) throws InvalidTransactionAmountException;
    List<Transaction> getTransactions(Status status, String accountNumber, LocalDate startDate, LocalDate endDate);
    TransactionSumary generateSummary(LocalDate date);
}
