package com.transactions.guama.use_cases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class GetAllTransactionsUseCase{

    private final TransactionsRepositoryInterface transacionsRepository;

    public GetAllTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public List<Transaction> getAll() {
        return transacionsRepository.findAll();
    }
}

