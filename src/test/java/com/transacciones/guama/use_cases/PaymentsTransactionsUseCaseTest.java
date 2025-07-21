package com.transacciones.guama.use_cases;

import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;
import com.transactions.guama.use_cases.PaymentsTransactionsUseCase;

@ExtendWith(MockitoExtension.class)
class PaymentsTransactionsUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private PaymentsTransactionsUseCase paymentsTransactionsUseCase;

    @BeforeEach
    void setUp() {
        paymentsTransactionsUseCase = new PaymentsTransactionsUseCase(transactionsRepository);
    }

    @Test
    void testPayTransactions_Success() throws ParseException {
        // Arrange
        double valorPago = 300.0;
        
        Transaction transaction1 = new Transaction();
        transaction1.setId("123");
        transaction1.setNombre("Compra Supermercado");
        transaction1.setFecha(Date.valueOf("2024-01-15"));
        transaction1.setValor(150.50);
        transaction1.setEstado(Transaction.Estado.No_Pagado);

        Transaction transaction2 = new Transaction();
        transaction2.setId("456");
        transaction2.setNombre("Gasolina");
        transaction2.setFecha(Date.valueOf("2024-01-16"));
        transaction2.setValor(75.25);
        transaction2.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);
        List<Transaction> expectedUpdatedTransactions = Arrays.asList(transaction1, transaction2);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);
        when(transactionsRepository.save(any(Transaction.class)))
            .thenAnswer(invocation -> {
                Transaction transaction = invocation.getArgument(0);
                transaction.setEstado(Transaction.Estado.Pagado);
                return transaction;
            });

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Transaction.Estado.Pagado, result.get(0).getEstado());
        assertEquals(Transaction.Estado.Pagado, result.get(1).getEstado());
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_InsufficientValue() throws ParseException {
        // Arrange
        double valorPago = 100.0; // Insuficiente para pagar todas las transacciones
        
        Transaction transaction1 = new Transaction();
        transaction1.setId("123");
        transaction1.setValor(150.50);
        transaction1.setEstado(Transaction.Estado.No_Pagado);

        Transaction transaction2 = new Transaction();
        transaction2.setId("456");
        transaction2.setValor(75.25);
        transaction2.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty()); // No se procesan transacciones si el valor es insuficiente
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_EmptyTransactionsList() throws ParseException {
        // Arrange
        double valorPago = 500.0;
        List<Transaction> emptyTransactions = Collections.emptyList();

        when(transactionsRepository.getTransactionsToPay()).thenReturn(emptyTransactions);

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_RepositoryThrowsException() throws ParseException {
        // Arrange
        double valorPago = 300.0;
        when(transactionsRepository.getTransactionsToPay())
            .thenThrow(new ParseException("Error parsing date", 0));

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            paymentsTransactionsUseCase.payTransactions(valorPago);
        });
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_ExactValue() throws ParseException {
        // Arrange
        double valorPago = 225.75; // Valor exacto para pagar ambas transacciones
        
        Transaction transaction1 = new Transaction();
        transaction1.setId("123");
        transaction1.setValor(150.50);
        transaction1.setEstado(Transaction.Estado.No_Pagado);

        Transaction transaction2 = new Transaction();
        transaction2.setId("456");
        transaction2.setValor(75.25);
        transaction2.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);
        when(transactionsRepository.save(any(Transaction.class)))
            .thenAnswer(invocation -> {
                Transaction transaction = invocation.getArgument(0);
                transaction.setEstado(Transaction.Estado.Pagado);
                return transaction;
            });

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(Transaction.Estado.Pagado, result.get(0).getEstado());
        assertEquals(Transaction.Estado.Pagado, result.get(1).getEstado());
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, times(2)).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_ZeroValue() throws ParseException {
        // Arrange
        double valorPago = 0.0;
        
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setValor(150.50);
        transaction.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Collections.singletonList(transaction);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testPayTransactions_NegativeValue() throws ParseException {
        // Arrange
        double valorPago = -50.0;
        
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setValor(150.50);
        transaction.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Collections.singletonList(transaction);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);

        // Act
        List<Transaction> result = paymentsTransactionsUseCase.payTransactions(valorPago);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testIsCorrectValueToPay_True() {
        // Arrange
        double valorPago = 300.0;
        
        Transaction transaction1 = new Transaction();
        transaction1.setValor(150.50);

        Transaction transaction2 = new Transaction();
        transaction2.setValor(75.25);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);

        // Act
        boolean result = paymentsTransactionsUseCase.isCorrectValueToPay(valorPago, transactionsToPay);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsCorrectValueToPay_False() {
        // Arrange
        double valorPago = 100.0;
        
        Transaction transaction1 = new Transaction();
        transaction1.setValor(150.50);

        Transaction transaction2 = new Transaction();
        transaction2.setValor(75.25);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);

        // Act
        boolean result = paymentsTransactionsUseCase.isCorrectValueToPay(valorPago, transactionsToPay);

        // Assert
        assertFalse(result);
    }

    @Test
    void testIsCorrectValueToPay_ExactValue() {
        // Arrange
        double valorPago = 225.75;
        
        Transaction transaction1 = new Transaction();
        transaction1.setValor(150.50);

        Transaction transaction2 = new Transaction();
        transaction2.setValor(75.25);

        List<Transaction> transactionsToPay = Arrays.asList(transaction1, transaction2);

        // Act
        boolean result = paymentsTransactionsUseCase.isCorrectValueToPay(valorPago, transactionsToPay);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsCorrectValueToPay_EmptyList() {
        // Arrange
        double valorPago = 100.0;
        List<Transaction> emptyTransactions = Collections.emptyList();

        // Act
        boolean result = paymentsTransactionsUseCase.isCorrectValueToPay(valorPago, emptyTransactions);

        // Assert
        assertTrue(result); // Cualquier valor es suficiente para una lista vacía
    }

    @Test
    void testIsCorrectValueToPay_ZeroValue() {
        // Arrange
        double valorPago = 0.0;
        
        Transaction transaction = new Transaction();
        transaction.setValor(150.50);

        List<Transaction> transactionsToPay = Collections.singletonList(transaction);

        // Act
        boolean result = paymentsTransactionsUseCase.isCorrectValueToPay(valorPago, transactionsToPay);

        // Assert
        assertFalse(result);
    }

    @Test
    void testPayTransactions_SaveMethodThrowsException() throws ParseException {
        // Arrange
        double valorPago = 300.0;
        
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setValor(150.50);
        transaction.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> transactionsToPay = Collections.singletonList(transaction);

        when(transactionsRepository.getTransactionsToPay()).thenReturn(transactionsToPay);
        when(transactionsRepository.save(any(Transaction.class)))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            paymentsTransactionsUseCase.payTransactions(valorPago);
        });
        
        verify(transactionsRepository, times(1)).getTransactionsToPay();
        verify(transactionsRepository, times(1)).save(any(Transaction.class));
    }
} 
