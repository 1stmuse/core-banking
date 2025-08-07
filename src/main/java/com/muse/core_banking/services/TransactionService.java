package com.muse.core_banking.services;

import com.muse.core_banking.dto.transaction.DepositRequestDto;
import com.muse.core_banking.dto.transaction.TransactionResponseDto;
import com.muse.core_banking.dto.transaction.WithdrawalRequestDto;
import com.muse.core_banking.entities.Transaction;
import com.muse.core_banking.enums.TransactionStatus;
import com.muse.core_banking.enums.TransactionType;
import com.muse.core_banking.repositories.AccountRepository;
import com.muse.core_banking.repositories.TransactionRepository;
import com.muse.core_banking.utils.Helpers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionResponseDto deposit(DepositRequestDto request, Long customerId) throws BadRequestException {
        var existingSourceAccount = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() ->  new BadRequestException("Account not found"));


        var transaction = Transaction.builder()
                .destinationAccount(existingSourceAccount.getAccountNo())
                .sourceAccount(request.fromAccount())
                .amount(request.amount())
                .type(TransactionType.DEPOSIT)
                .status(TransactionStatus.PENDING)
                .transactionRef(Helpers.generateTransactionRef())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(existingSourceAccount);
        return new TransactionResponseDto(transaction.getTransactionRef());
    }

    public TransactionResponseDto withdraw(WithdrawalRequestDto request, Long customerId) throws BadRequestException {
        var existingSourceAccount = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() -> new BadRequestException("Account not found") );

        if(existingSourceAccount.getBalance() < request.amount()){
            throw new BadRequestException("Insufficient Balance");
        }

        var transaction = Transaction.builder()
                .destinationAccount(request.destinationAcc())
                .sourceAccount(existingSourceAccount.getAccountNo())
                .amount(request.amount())
                .type(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.PENDING)
                .transactionRef(Helpers.generateTransactionRef())
                .build();

        transactionRepository.save(transaction);
        accountRepository.save(existingSourceAccount);
        return new TransactionResponseDto(transaction.getTransactionRef());
    }

    @Transactional
    public TransactionResponseDto internalTransfer(WithdrawalRequestDto request, Long customerId) throws BadRequestException {
        var existingSourceAccount = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow( () -> new BadRequestException("Account not found"));
        var existingDestinationAccount = accountRepository.findByAccountNo(request.destinationAcc())
                .orElseThrow(() -> new BadRequestException("Destination Account not found") );

        if(existingSourceAccount.getBalance() < request.amount()){
            throw new BadRequestException("Insufficient Balance");
        }

        existingDestinationAccount.setBalance(existingDestinationAccount.getBalance() + request.amount());
        existingSourceAccount.setBalance(existingSourceAccount.getBalance() - request.amount());

        accountRepository.saveAll(List.of(existingDestinationAccount, existingSourceAccount));

        var transaction = Transaction.builder()
                .destinationAccount(request.destinationAcc())
                .sourceAccount(existingSourceAccount.getAccountNo())
                .amount(request.amount())
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.SUCCESS)
                .transactionRef(Helpers.generateTransactionRef())
                .build();
        transactionRepository.save(transaction);

        return new TransactionResponseDto(transaction.getTransactionRef());
    }
}
