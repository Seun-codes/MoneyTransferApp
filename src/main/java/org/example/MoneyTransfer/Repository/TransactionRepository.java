package org.example.MoneyTransfer.Repository;

import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByStatus(Status status);

    List<Transaction> findBySourceAccountNumberOrDestinationAccountNumber(String source, String destination);

    @Query("SELECT t FROM Transaction t WHERE t.dateCreated BETWEEN :start AND :end")
    List<Transaction> findByDateCreatedBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);


    @Query("SELECT t FROM Transaction t WHERE DATE(t.dateCreated) = :date")
    List<Transaction> findByDate(@Param("date") LocalDate date);


    @Query("SELECT t FROM Transaction t WHERE t.status = org.example.MoneyTransfer.enumeration.Status.SUCCESSFUL AND (t.commissionWorthy = false OR t.commissionWorthy IS NULL)")

    List<Transaction> findSuccessfulUncommissionedTransactions();

    Optional<Transaction> findByTransactionReference(String transactionReference);



}