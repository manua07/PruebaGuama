package com.transactions.guama.use_cases;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class GetTransactionsUseCase {

    private final TransactionsRepositoryInterface transacionsRepository;

    public GetTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public Transaction getTransaction(String id) throws ParseException {
        return transacionsRepository.findTransaction(id);
    }

}
