package org.example.MoneyTransfer.Exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 29,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidAccountException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAccount(InvalidAccountException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 30,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(FailedTransactionException.class)
    public ResponseEntity<Map<String, Object>> handleFailedTransaction(FailedTransactionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 31,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidTransactionRefException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTransactionRef(InvalidTransactionRefException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 32,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientAmount(InsufficientBalanceException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 34,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAccountNotFound(AccountNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 35,
                        "responseMessage", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(InvalidTransactionAmountException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAmount(InvalidTransactionAmountException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "responseCode", 36,
                        "responseMessage", ex.getMessage()
                )
        );
    }
}
