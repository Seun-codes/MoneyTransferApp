package org.example.MoneyTransfer.service;

import org.example.MoneyTransfer.Exception.*;
import org.example.MoneyTransfer.domain.Account;
import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.enumeration.Status;
import lombok.RequiredArgsConstructor;
import org.example.MoneyTransfer.Repository.AccountRepository;
import org.example.MoneyTransfer.Repository.TransactionRepository;
import org.example.MoneyTransfer.dto.TransactionSumary;
import org.example.MoneyTransfer.dto.TransferRequest;
import org.example.MoneyTransfer.dto.TransferResponse;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Override
    public TransferResponse transfer(TransferRequest request)  {

        String sourceAccountNumber = request.getSourceAccountNumber();
        String destinationAccountNumber = request.getDestinationAccountNumber();

        if (sourceAccountNumber == null || destinationAccountNumber == null ||
                sourceAccountNumber.isEmpty() || destinationAccountNumber.isEmpty()) {
            throw new InvalidAccountException("Source or destination account number is missing or empty.");
        }
        if (destinationAccountNumber.length() != 10 || !destinationAccountNumber.matches("\\d{10}")) {
            throw new InvalidAccountException("Destination account number must be exactly 10 digits.");
        }

        BigDecimal amount = request.getAmount();


        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionAmountException("Transfer amount must be greater than zero.");
        }

        BigDecimal transactionFee = amount.multiply(BigDecimal.valueOf(0.005));
        if (transactionFee.compareTo(BigDecimal.valueOf(100)) > 0) {
            transactionFee = BigDecimal.valueOf(100);
        }


        String transactionRef = request.getTransactionRef();
        if (transactionRef == null || transactionRef.length() < 12) {
            throw new InvalidTransactionRefException("Transaction reference must be at least 12 characters long.");
        }

        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionReference(transactionRef);

        if (existingTransaction.isPresent()) {
            throw new InvalidTransactionRefException("Transaction reference already exists.");
        }
        Optional<Account> sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        if (!sourceAccount.isPresent()) {
            throw new AccountNotFoundException("Source account not found.");
        }

        BigDecimal billedAmount = amount.add(transactionFee);
        BigDecimal sourceAccountBalance = sourceAccount.get().getBalance();

        if (sourceAccountBalance.compareTo(billedAmount) < 0) {
            // Insufficient balance, simulate failed response
            Transaction failedTransaction = new Transaction();
            failedTransaction.setTransactionReference(request.getTransactionRef());
            failedTransaction.setSourceAccountNumber(sourceAccountNumber);
            failedTransaction.setDestinationAccountNumber(destinationAccountNumber);
            failedTransaction.setAmount(amount);
            failedTransaction.setTransactionFee(BigDecimal.ZERO);
            failedTransaction.setCommissionWorthy(false);
            failedTransaction.setDescription("Insufficient funds in source account");
            failedTransaction.setStatus(Status.FAILED);
            failedTransaction.setStatusMessage("Transaction failed due to insufficient balance.");
            failedTransaction.setDateCreated(LocalDateTime.now());
            transactionRepository.save(failedTransaction);

            return TransferResponse.builder()
                    .transactionReference(failedTransaction.getTransactionReference())
                    .status(failedTransaction.getStatus())
                    .message(failedTransaction.getStatusMessage())
                    .responseCode("99")
                    .build();
        }

        // Deduct the transfer amount from the source account balance
        Account sourceAccountToUpdate = sourceAccount.get();
        sourceAccountToUpdate.setBalance(sourceAccountBalance.subtract(billedAmount));
        accountRepository.save(sourceAccountToUpdate);



        Transaction transaction = new Transaction();
        transaction.setTransactionReference(request.getTransactionRef());
        transaction.setSourceAccountNumber(sourceAccountNumber);
        transaction.setDestinationAccountNumber(destinationAccountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionFee(transactionFee);
        transaction.setDescription(request.getDescription());
        transaction.setDateCreated(LocalDateTime.now());

        boolean isTransactionSuccessful = true;

        if (isTransactionSuccessful) {

            transaction.setStatus(Status.SUCCESSFUL);
            transaction.setStatusMessage(Status.SUCCESSFUL.getDefaultMessage());
            transaction.setCommissionWorthy(false);
            transaction.setBilledAmount(billedAmount);
            transactionRepository.save(transaction);
        } else {

            transaction.setStatus(Status.FAILED);
            transaction.setStatusMessage(Status.FAILED.getDefaultMessage());
            transaction.setCommissionWorthy(false);
            transactionRepository.save(transaction);
        }


        transactionRepository.save(transaction);

        boolean isSuccessful = transaction.getStatus() == Status.SUCCESSFUL;

        if (isSuccessful) {
            return TransferResponse.builder()
                    .transactionReference(transaction.getTransactionReference())
                    .status(transaction.getStatus())
                    .message(transaction.getStatusMessage())
                    .billedAmount(billedAmount)
                    .responseCode("00")
                    .build();
        } else {
            return TransferResponse.builder()
                    .transactionReference(transaction.getTransactionReference())
                    .status(transaction.getStatus())
                    .message(transaction.getStatusMessage())
                    .responseCode("99")
                    .build();

        }
    }

    @Override
    public List<Transaction> getTransactions(Status status, String accountNumber, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findAll();
        if (status != null) {
            transactions = transactions.stream()
                    .filter(tx -> tx.getStatus() == status)
                    .toList();
        }

        if (accountNumber != null && !accountNumber.isEmpty()) {
            transactions = transactions.stream()
                    .filter(tx -> accountNumber.equalsIgnoreCase(tx.getSourceAccountNumber())
                            || accountNumber.equalsIgnoreCase(tx.getDestinationAccountNumber()))
                    .toList();
        }

        if (startDate != null && endDate != null) {
            transactions = transactions.stream()
                    .filter(tx -> {
                        LocalDate txDate = tx.getDateCreated().toLocalDate();
                        return (txDate.isEqual(startDate) || txDate.isAfter(startDate)) &&
                                (txDate.isEqual(endDate) || txDate.isBefore(endDate));
                    })
                    .toList();
        }
        return transactions;
    }

    @Override
    public TransactionSumary generateSummary(LocalDate date) {
        List<Transaction> transactions = transactionRepository.findByDate(date);

        int totalTransactions = transactions.size();
        BigDecimal totalAmount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalFees = transactions.stream()
                .map(Transaction::getTransactionFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCommission = transactions.stream()
                .filter(t -> Boolean.TRUE.equals(t.getCommissionWorthy()))
                .map(Transaction::getCommission)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TransactionSumary.builder()
                .totalTransactions(totalTransactions)
                .totalAmount(totalAmount)
                .totalFees(totalFees)
                .totalCommission(totalCommission)
                .build();
    }


}
