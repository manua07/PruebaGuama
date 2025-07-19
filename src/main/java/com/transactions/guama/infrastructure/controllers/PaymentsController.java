package com.transactions.guama.infrastructure.controllers;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.use_cases.PaymentsTransactionsUseCase;

@RestController
public class PaymentsController {

    private final PaymentsTransactionsUseCase paymentsTransactionsUseCase;

    public PaymentsController(PaymentsTransactionsUseCase paymentsTransactionsUseCase) {
        this.paymentsTransactionsUseCase = paymentsTransactionsUseCase;
    }

    @PostMapping(value = "/api/payments", params = "valor", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Transaction> paymentTransaction(double valor) throws ParseException {
        return paymentsTransactionsUseCase.payTransactions(valor);
    }

}
