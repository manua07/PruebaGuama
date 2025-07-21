package com.transacciones.guama.use_cases;

import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;
import com.transactions.guama.use_cases.GetAllTransactionsUseCase;

@ExtendWith(MockitoExtension.class)
class GetAllTransactionsUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private GetAllTransactionsUseCase getAllTransactionsUseCase;

    @BeforeEach
    void setUp() {
        getAllTransactionsUseCase = new GetAllTransactionsUseCase(transactionsRepository);
    }

    @Test
    void testGetAll_WithTransactions() throws ParseException {
        // Arrange
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
        transaction2.setEstado(Transaction.Estado.Pagado);

        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction2);

        when(transactionsRepository.findAll()).thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = getAllTransactionsUseCase.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("123", result.get(0).getId());
        assertEquals("456", result.get(1).getId());
        assertEquals("Compra Supermercado", result.get(0).getNombre());
        assertEquals("Gasolina", result.get(1).getNombre());
        
        verify(transactionsRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_EmptyList() throws ParseException {
        // Arrange
        when(transactionsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Transaction> result = getAllTransactionsUseCase.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(transactionsRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_RepositoryThrowsException() throws ParseException {
        // Arrange
        when(transactionsRepository.findAll()).thenThrow(new ParseException("Error parsing date", 0));

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            getAllTransactionsUseCase.getAll();
        });
        
        verify(transactionsRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_SingleTransaction() throws ParseException {
        // Arrange
        Transaction transaction = new Transaction();
        transaction.setId("789");
        transaction.setNombre("Restaurante");
        transaction.setFecha(Date.valueOf("2024-01-17"));
        transaction.setValor(45.00);
        transaction.setEstado(Transaction.Estado.No_Pagado);

        List<Transaction> expectedTransactions = Collections.singletonList(transaction);

        when(transactionsRepository.findAll()).thenReturn(expectedTransactions);

        // Act
        List<Transaction> result = getAllTransactionsUseCase.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("789", result.get(0).getId());
        assertEquals("Restaurante", result.get(0).getNombre());
        assertEquals(45.00, result.get(0).getValor());
        assertEquals(Transaction.Estado.No_Pagado, result.get(0).getEstado());
        
        verify(transactionsRepository, times(1)).findAll();
    }

    @Test
    void testGetAll_NullReturnFromRepository() throws ParseException {
        // Arrange
        when(transactionsRepository.findAll()).thenReturn(null);

        // Act
        List<Transaction> result = getAllTransactionsUseCase.getAll();

        // Assert
        assertNull(result);
        
        verify(transactionsRepository, times(1)).findAll();
    }
} 
