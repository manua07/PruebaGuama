package com.transactions.guama.domain;

import java.util.List;

public interface TransactionsRepositoryInterface {

    Transaction save(Transaction transaction);

    Transaction delete(String id);
    
    List<Transaction> findAll();

    Transaction findTransaction(String id);

}
