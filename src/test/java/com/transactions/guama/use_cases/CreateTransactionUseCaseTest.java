package com.transactions.guama.use_cases;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@ExtendWith(MockitoExtension.class)
class CreateTransactionUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private CreateTransactionUseCase createTransactionUseCase;

    @BeforeEach
    void setUp() {
        createTransactionUseCase = new CreateTransactionUseCase(transactionsRepository);
    }

    @Test
    void testCreateTransaction_Success() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setNombre("Compra Supermercado");
        transaction.setFecha(Date.valueOf("2024-01-15"));
        transaction.setValor(150.50);
        transaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("Compra Supermercado", result.getNombre());
        assertEquals(150.50, result.getValor());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_RepositoryReturnsNull() {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId("123");
        transaction.setNombre("Test Transaction");

        when(transactionsRepository.save(any(Transaction.class))).thenReturn(null);

        // Act
        Transaction result = createTransactionUseCase.create(transaction);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).save(transaction);
    }

    @Test
    void testCreateTransaction_WithDifferentStates() {
        // Arrange
        Transaction transactionPagado = new Transaction();
        transactionPagado.setId("123");
        transactionPagado.setEstado(Transaction.Estado.Pagado);

        Transaction transactionNoPagado = new Transaction();
        transactionNoPagado.setId("456");
        transactionNoPagado.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.save(transactionPagado)).thenReturn(transactionPagado);
        when(transactionsRepository.save(transactionNoPagado)).thenReturn(transactionNoPagado);

        // Act
        Transaction resultPagado = createTransactionUseCase.create(transactionPagado);
        Transaction resultNoPagado = createTransactionUseCase.create(transactionNoPagado);

        // Assert
        assertEquals(Transaction.Estado.Pagado, resultPagado.getEstado());
        assertEquals(Transaction.Estado.No_Pagado, resultNoPagado.getEstado());
        
        verify(transactionsRepository, times(2)).save(any(Transaction.class));
    }
} 