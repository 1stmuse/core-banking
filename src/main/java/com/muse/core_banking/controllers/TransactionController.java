package com.muse.core_banking.controllers;

import com.muse.core_banking.dto.transaction.DepositRequestDto;
import com.muse.core_banking.dto.transaction.TransactionHistoryQuery;
import com.muse.core_banking.dto.transaction.WithdrawalRequestDto;
import com.muse.core_banking.enums.ApiMessage;
import com.muse.core_banking.handlers.ResponseHandler;
import com.muse.core_banking.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.muse.core_banking.enums.ApiMessage.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @RequestBody DepositRequestDto requestDto,
            @RequestParam String customerId
            ) throws BadRequestException {

        var response = transactionService.deposit(requestDto, Long.parseLong(customerId));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .data(Optional.of(response))
                        .build()
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestBody WithdrawalRequestDto requestDto,
            @RequestParam String customerId
    ) throws BadRequestException {

        var response = transactionService.withdraw(requestDto, Long.parseLong(customerId));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .data(Optional.of(response))
                        .build()
        );
    }

    @PostMapping("/transfers/internal")
    public ResponseEntity<?> internalTransfer(
            @RequestBody WithdrawalRequestDto requestDto,
            @RequestParam String customerId
    ) throws BadRequestException {

        var response = transactionService.internalTransfer(requestDto, Long.parseLong(customerId));
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .data(Optional.of(response))
                        .build()
        );
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getTransactions(
            @PathVariable String customerId,
            TransactionHistoryQuery query
    ) throws BadRequestException {
        var response = transactionService.transactionHistory(Long.parseLong(customerId), query);
        return ResponseEntity.ok(
                ResponseHandler.builder()
                        .message(SUCCESS.getMessage())
                        .statusCode(OK.value())
                        .data(Optional.of(response))
                        .build()
        );
    }
}
