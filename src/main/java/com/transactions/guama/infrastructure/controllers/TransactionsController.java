package com.transactions.guama.infrastructure.controllers;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.infrastructure.controllers.forms.TransactionsDataForm;
import com.transactions.guama.use_cases.CreateTransactionUseCase;
import com.transactions.guama.use_cases.DeleteTransactionsUseCase;
import com.transactions.guama.use_cases.EditTransactionsUseCase;
import com.transactions.guama.use_cases.GetAllTransactionsUseCase;
import com.transactions.guama.use_cases.GetTransactionsUseCase;


@CrossOrigin(origins = "http://localhost:3000", 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController

public class TransactionsController {

    private final CreateTransactionUseCase createTransactionUseCase;

    private final GetAllTransactionsUseCase getAllTransactionsUseCase;

    private final GetTransactionsUseCase getTransactionsUseCase;

    private final DeleteTransactionsUseCase deleteTransactionsUseCase;

    private final EditTransactionsUseCase editTransactionsUseCase;


    public TransactionsController(CreateTransactionUseCase createTransactionUseCase, GetAllTransactionsUseCase getAllTransactionsUseCase, DeleteTransactionsUseCase deleteTransactionsUseCase, GetTransactionsUseCase getTransactionsUseCase, EditTransactionsUseCase editTransactionsUseCase) {
        this.createTransactionUseCase = createTransactionUseCase;
        this.getAllTransactionsUseCase = getAllTransactionsUseCase;
        this.getTransactionsUseCase = getTransactionsUseCase;
        this.deleteTransactionsUseCase = deleteTransactionsUseCase;
        this.editTransactionsUseCase = editTransactionsUseCase;
    }

    @PostMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Transaction createTransaction(@RequestBody TransactionsDataForm createTransactionsDataForm) {
        return createTransactionUseCase.create(createTransactionsDataForm.toTransactions());
    }

    @GetMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Transaction> GetAllTransactions() throws ParseException {
        return getAllTransactionsUseCase.getAll();
    }

    @GetMapping(value = "/api/transactions", params = "id", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Transaction GetTransactions(@RequestParam String id) throws ParseException {
        Transaction getTransaction = getTransactionsUseCase.getTransaction(id);
        return getTransaction;
    }

    @DeleteMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Transaction deleteTransaction(@RequestParam String id) throws ParseException {
        Transaction deleted = deleteTransactionsUseCase.delete(id);
        return deleted;
    }

    @PutMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Transaction editTransaction(@RequestBody TransactionsDataForm createTransactionsDataForm) throws ParseException {
        return editTransactionsUseCase.editTransaction(createTransactionsDataForm.toTransactions());
    }

    
}


