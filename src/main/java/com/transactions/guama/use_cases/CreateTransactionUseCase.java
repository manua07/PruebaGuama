package com.transactions.guama.use_cases;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class CreateTransactionUseCase{

    private final TransactionsRepositoryInterface transactionsRepository;

    public CreateTransactionUseCase(TransactionsRepositoryInterface transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    public Transaction create(Transaction transaction) {
        return transactionsRepository.save(transaction);
    }
}

