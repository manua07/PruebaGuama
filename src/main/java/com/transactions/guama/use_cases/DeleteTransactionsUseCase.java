package com.transactions.guama.use_cases;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class DeleteTransactionsUseCase {

private final TransactionsRepositoryInterface transacionsRepository;

    public DeleteTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public Transaction delete(String id) {
        Transaction transactiondelete = transacionsRepository.findTransaction(id);
        if (transactiondelete != null){
            if ("Pagado".equalsIgnoreCase(transactiondelete.getEstado())) {
                throw new IllegalStateException("No se puede eliminar una transacción que ya está pagada.");
            }
        }
        return transactiondelete;
    }
}
