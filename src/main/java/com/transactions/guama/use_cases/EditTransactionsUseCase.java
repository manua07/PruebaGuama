package com.transactions.guama.use_cases;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class EditTransactionsUseCase {

private final TransactionsRepositoryInterface transacionsRepository;

    public EditTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public Transaction editTransaction(Transaction transaction) {
        Transaction savedtransaction = null;
        Transaction transactionget = transacionsRepository.findTransaction(transaction.getId());

        if (transactionget != null){
            if ("Pagado".equalsIgnoreCase(transactionget.getEstado())) {
                throw new IllegalStateException("No se puede editar una transacción que ya está pagada.");
            }
            savedtransaction = transacionsRepository.save(transaction);
        }

        return savedtransaction;

    }


}
