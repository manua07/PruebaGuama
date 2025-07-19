package com.transactions.guama.infrastructure.database.repositories;

import java.text.ParseException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;
import com.transactions.guama.persistence.DynamoDBConnection;

@Repository
public class TransactionsRepository implements TransactionsRepositoryInterface {

    private DynamoDBConnection dynamoConnection;

    @Override
    public Transaction save(Transaction transaction) {
        dynamoConnection = new DynamoDBConnection();
        dynamoConnection.initDBDynamo();
        dynamoConnection.saveTransaction(transaction);
        dynamoConnection.closeDynamoConnection();
        return transaction;
    }

    @Override
    public List<Transaction> findAll() throws ParseException {
        dynamoConnection = new DynamoDBConnection();
        dynamoConnection.initDBDynamo();
        List<Transaction> transactions = dynamoConnection.getAllTransactions();
        dynamoConnection.closeDynamoConnection();
        return transactions;
    }

    @Override
    public Transaction delete(String id) {
        dynamoConnection = new DynamoDBConnection();
        dynamoConnection.initDBDynamo();
        dynamoConnection.deleteTransaction(id);
        dynamoConnection.closeDynamoConnection();
        return null;
    }

    @Override
    public Transaction findTransaction(String id) throws ParseException {
        dynamoConnection = new DynamoDBConnection();
        dynamoConnection.initDBDynamo();
        Transaction transaction= dynamoConnection.getTransaction(id);
        dynamoConnection.closeDynamoConnection();
        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsToPay() throws ParseException {
        dynamoConnection = new DynamoDBConnection();
        dynamoConnection.initDBDynamo();
        List<Transaction> transactions = dynamoConnection.getTransactionsToPay();
        dynamoConnection.closeDynamoConnection();
        return transactions;
    }
}
