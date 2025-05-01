package org.example.MoneyTransfer.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.MoneyTransfer.Repository.TransactionRepository;
import org.example.MoneyTransfer.domain.Transaction;
import org.example.MoneyTransfer.dto.TransactionSumary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SchedulerServiceImpl implements SchedulerService {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    @Override
    @Scheduled(cron = "${commission.job.cron}" )
    public void generateDailySummary() {
        LocalDate day = LocalDate.now();
        TransactionSumary summary = transactionService.generateSummary(day);
        log.info("Daily Summary for {}: {}",day, summary);

    }

    @Scheduled(cron = "${logs.job.cron}")
    public void logTransactions() {
        List<Transaction> transactions = transactionService.getTransactions(null, null, null, null);
        log.info("==== Transaction Log ({} total) ====", transactions.size());
        transactions.forEach(tx -> log.info("{}", tx));
    }

    @Override
    @Scheduled(cron = "${summary.job.cron}")
//    @Scheduled(fixedRate = 60000)
    public void processCommission() {
        List<Transaction> transactions = transactionRepository.findSuccessfulUncommissionedTransactions();

        for (Transaction tx : transactions) {
            if (tx.getTransactionFee() != null) {
                BigDecimal commission = tx.getTransactionFee()
                        .multiply(BigDecimal.valueOf(0.20))
                        .setScale(2, RoundingMode.HALF_UP);

                tx.setCommissionWorthy(true);
                tx.setCommission(commission);
                transactionRepository.save(tx);
            }
        }

        transactionRepository.saveAll(transactions);

        log.info("Processed commission for {} transactions.", transactions.size());

    }


}
