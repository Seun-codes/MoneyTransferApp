package org.example.MoneyTransfer.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.MoneyTransfer.enumeration.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String transactionReference;

    private String sourceAccountNumber;

    private String destinationAccountNumber;

    private BigDecimal amount;

    private BigDecimal transactionFee;

    private BigDecimal billedAmount;

    private String description;

    private LocalDateTime dateCreated;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String statusMessage;

    @Column(name = "commission_worthy")
    private Boolean commissionWorthy;

    private BigDecimal commission;
}
