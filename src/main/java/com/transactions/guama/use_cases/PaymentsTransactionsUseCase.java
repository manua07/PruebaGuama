package com.transactions.guama.use_cases;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@Service
public class PaymentsTransactionsUseCase {

    private final TransactionsRepositoryInterface transacionsRepository;

    public PaymentsTransactionsUseCase(TransactionsRepositoryInterface transacionsRepository) {
        this.transacionsRepository = transacionsRepository;
    }

    public List<Transaction> payTransactions(double valor) throws ParseException {
        List<Transaction> transactionsToPay = transacionsRepository.getTransactionsToPay();
        List<Transaction> updatedTransactions = new ArrayList<>();
        Transaction savedtransaction;

        if (isCorrectValueToPay(valor, transactionsToPay)){
        for (Transaction transaction : transactionsToPay) {
            transaction.setEstado(Transaction.Estado.Pagado);
            savedtransaction = transacionsRepository.save(transaction);
            updatedTransactions.add(savedtransaction);
            }
        }
        return updatedTransactions;
    }

    public Boolean isCorrectValueToPay(double valor, List<Transaction> transactionsToPay){
        double valueToPay = 0;

        for (Transaction transaction : transactionsToPay) {
            valueToPay += transaction.getValor();
        }

        return valueToPay <= valor;

    }



}
