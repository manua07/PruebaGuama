package com.transactions.guama.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.transactions.guama.domain.Transaction;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ReturnConsumedCapacity;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;


public class DynamoDBConnection {
    private DynamoDbClient dynamoDbClient;

    public void initDBDynamo(){
        dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_2)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void saveTransaction(Transaction transaction){
        PutItemResponse response = dynamoDbClient.putItem(PutItemRequest.builder().item(Map.of(
            "id", AttributeValue.builder().s(transaction.getId()).build(),
            "Nombre", AttributeValue.builder().s(transaction.getNombre()).build(),
            "Fecha", AttributeValue.builder().s(transaction.getFecha()).build(),
            "Valor", AttributeValue.builder().n(String.valueOf(transaction.getValor())).build(),
            "Estado", AttributeValue.builder().s(transaction.getEstado()).build()
            
            )).returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
                .tableName("TransactionsGuama")
                .build());
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactions = new ArrayList<>();

        ScanRequest scanRequest = ScanRequest.builder()
            .tableName("TransactionsGuama")
            .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        for (Map<String, AttributeValue> item : scanResponse.items()) {
            Transaction transaction = new Transaction();
            transaction.setId(item.get("id").s());
            transaction.setNombre(item.get("Nombre").s());
            transaction.setFecha(item.get("Fecha").s());
            transaction.setValor(Double.valueOf(item.get("Valor").n()));
            transaction.setEstado(item.get("Estado").s());

            transactions.add(transaction);
        }
        return transactions;
    }

    public Transaction getTransaction(String id){
        Map<String, AttributeValue> key = Map.of(
        "id", AttributeValue.builder().s(id).build()

        );

        GetItemRequest request = GetItemRequest.builder()
            .tableName("TransactionsGuama")
            .key(key)
            .build();

        GetItemResponse response = dynamoDbClient.getItem(request);

        if (!response.hasItem()) {
            return null;
        }

        Map<String, AttributeValue> item = response.item();
            Transaction transaction = new Transaction();
            transaction.setId(item.get("id").s());
            transaction.setNombre(item.get("Nombre").s());
            transaction.setFecha(item.get("Fecha").s());
            transaction.setValor(Double.valueOf(item.get("Valor").n()));
            transaction.setEstado(item.get("Estado").s());
        
        return transaction;
    }

    public void deleteTransaction(String id){
        String partitionKeyName = "id";

        DeleteItemRequest deleteRequest = DeleteItemRequest.builder()
            .tableName("TransactionsGuama")
            .key(Map.of(
                    partitionKeyName, AttributeValue.builder().s(id).build()
                ))
                .build();

        dynamoDbClient.deleteItem(deleteRequest);
        System.out.println("Transacción con id " + id + " eliminada correctamente.");
    }

    public void closeDynamoConnection(){
        dynamoDbClient.close();
    }

    
}
