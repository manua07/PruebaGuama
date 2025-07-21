package com.transactions.guama.use_cases;

import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class DeleteTransactionsUseCase {

private final TransactionsRepositoryInterface transacionsRepository;

    public DeleteTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public Transaction delete(String id) throws ParseException {
        Transaction transactiondelete = transacionsRepository.findTransaction(id);
        if (transactiondelete != null){
            if (Transaction.Estado.Pagado == transactiondelete.getEstado()){
                throw new IllegalStateException("No se puede eliminar una transacción que ya está pagada.");
            }
        }
        transacionsRepository.delete(id);
        return transactiondelete;
    }
}
