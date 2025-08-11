package com.muse.core_banking.services;

import com.muse.core_banking.dto.transaction.*;
import com.muse.core_banking.entities.Customer;
import com.muse.core_banking.entities.Transaction;
import com.muse.core_banking.enums.AccountStatus;
import com.muse.core_banking.enums.TransactionStatus;
import com.muse.core_banking.enums.TransactionType;
import com.muse.core_banking.mappers.TransactionMapper;
import com.muse.core_banking.repositories.AccountRepository;
import com.muse.core_banking.repositories.CustomerRepository;
import com.muse.core_banking.repositories.TransactionRepository;
import com.muse.core_banking.utils.Helpers;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionMapper transactionMapper;

    private final LocalDate today = LocalDate.now();
    private final LocalDate weekAgo = today.minusDays(7);

    public TransactionHistoryDto transactionHistory(Long customerId, TransactionHistoryQuery query) throws BadRequestException {
        var customerDetails = customerRepository.findById(customerId)
                .orElseThrow(() -> new  BadRequestException("Customer details not found"));

        Pageable pageRequest = PageRequest.of(query.page() - 1, query.perPage(), Sort.by("createdAt").descending());

        Page<Transaction> transactions = transactionRepository.findByAccount(customerDetails.getAccount().getAccountNo(), pageRequest);

        System.out.println("customerACCount" + customerDetails.getAccount().getAccountNo().toString());
        System.out.println("transaction List" + transactions.get().toList());
        log.warn("DTATA", customerDetails.getAccount().toString(), transactions.get().toList().toString());

        var transactionList = transactions.map(transactionMapper::toTransactionDetail).stream().toList();

        return new TransactionHistoryDto(transactionList, new PageResult(query.page(), query.perPage(), transactions.getTotalPages()));
    }

    public TransactionResponseDto deposit(DepositRequestDto request, Long customerId) throws BadRequestException {
        var existingSourceAccount = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow(() ->  new BadRequestException("Account not found"));

        if(existingSourceAccount.getAccountStatus() == AccountStatus.INACTIVE){
            throw new BadRequestException("Account inactive, please contact admin");
        }

        existingSourceAccount.setBalance(existingSourceAccount.getBalance() + request.amount());


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

        if(existingSourceAccount.getAccountStatus() == AccountStatus.INACTIVE){
            throw new BadRequestException("Account inactive, please contact admin");
        }

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
        var customerDetails = customerRepository.findById(customerId)
                .orElseThrow(() -> new  BadRequestException("Customer details not found"));
        var existingSourceAccount = accountRepository.findByCustomer_Id(customerId)
                .orElseThrow( () -> new BadRequestException("Account not found"));
        var existingDestinationAccount = accountRepository.findByAccountNo(request.destinationAcc())
                .orElseThrow(() -> new BadRequestException("Destination Account not found") );

        if(existingSourceAccount.getAccountStatus() == AccountStatus.INACTIVE){
            throw new BadRequestException("Account inactive, please contact admin");
        }

        if(existingDestinationAccount.getAccountStatus() == AccountStatus.INACTIVE){
            throw new BadRequestException("Account inactive, please contact admin");
        }

        if(existingSourceAccount.getBalance() < request.amount()){
            throw new BadRequestException("Insufficient Balance");
        }

        getAndCalculateTransactionLimitCheck(existingSourceAccount.getAccountNo(), request.amount(), customerDetails);

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

    private Long sumTransactionAmount(List<Transaction> transactions){
        var amount = 0L;
        for (var t: transactions){
            amount += t.getAmount();
        }
        return amount;
    }

    private void getAndCalculateTransactionLimitCheck(Long sourceAccountNo, Long amount, Customer customerDetails) throws BadRequestException {
        Date startDate = Date.from(weekAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        var dailyTransactionList = transactionRepository.findTodayTransactions(sourceAccountNo);
        var weeklyTransactionList = transactionRepository.findTransactionsWithinPeriod(startDate, endDate, sourceAccountNo);
        var dailyTransactionAmount = sumTransactionAmount(dailyTransactionList);
        var weeklyTransactionAmount = sumTransactionAmount(weeklyTransactionList);

        if(dailyTransactionAmount >= customerDetails.getDailyTransactionLimit()
                || (dailyTransactionAmount + amount) >= customerDetails.getDailyTransactionLimit()
        ){
            throw new BadRequestException("Daily transaction limit exceeded");
        }

        if(weeklyTransactionAmount >= customerDetails.getWeeklyTransactionLimit()
                || (weeklyTransactionAmount + amount) >= customerDetails.getWeeklyTransactionLimit()
        ){
            throw new BadRequestException("Weekly transaction limit exceeded");
        }
    }
}
