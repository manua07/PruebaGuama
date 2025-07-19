package com.transactions.guama.use_cases;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class GetTransactionsUseCase {

    private final TransactionsRepositoryInterface transacionsRepository;

    public GetTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public Transaction getTransaction(String id) {
        return transacionsRepository.findTransaction(id);
    }

}
