package com.muse.core_banking.mappers;

import com.muse.core_banking.dto.transaction.TransactionDetailDto;
import com.muse.core_banking.entities.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionMapper {

    public TransactionDetailDto toTransactionDetail(Transaction transaction){

        return new TransactionDetailDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getSourceAccount(),
                transaction.getDestinationAccount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getTransactionRef(),
                transaction.getCreatedAt()
        );
    }
}
