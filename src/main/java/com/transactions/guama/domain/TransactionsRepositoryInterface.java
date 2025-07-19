package com.transactions.guama.domain;

import java.text.ParseException;
import java.util.List;

public interface TransactionsRepositoryInterface {

    Transaction save(Transaction transaction);

    Transaction delete(String id);
    
    List<Transaction> findAll() throws ParseException;

    Transaction findTransaction(String id) throws ParseException;

    List<Transaction> getTransactionsToPay() throws ParseException;


}
