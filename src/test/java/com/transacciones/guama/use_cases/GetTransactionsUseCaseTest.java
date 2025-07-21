package com.transacciones.guama.use_cases;

import java.sql.Date;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.transactions.guama.use_cases.GetTransactionsUseCase;

@ExtendWith(MockitoExtension.class)
class GetTransactionsUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private GetTransactionsUseCase getTransactionsUseCase;

    @BeforeEach
    void setUp() {
        getTransactionsUseCase = new GetTransactionsUseCase(transactionsRepository);
    }

    @Test
    void testGetTransaction_Success() throws ParseException {
        // Arrange
        String transactionId = "123";
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId(transactionId);
        expectedTransaction.setNombre("Compra Supermercado");
        expectedTransaction.setFecha(Date.valueOf("2024-01-15"));
        expectedTransaction.setValor(150.50);
        expectedTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(expectedTransaction);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals("Compra Supermercado", result.getNombre());
        assertEquals(150.50, result.getValor());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }

    @Test
    void testGetTransaction_NotFound() throws ParseException {
        // Arrange
        String transactionId = "999";
        when(transactionsRepository.findTransaction(transactionId)).thenReturn(null);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }

    @Test
    void testGetTransaction_RepositoryThrowsException() throws ParseException {
        // Arrange
        String transactionId = "123";
        when(transactionsRepository.findTransaction(transactionId))
            .thenThrow(new ParseException("Error parsing date", 0));

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            getTransactionsUseCase.getTransaction(transactionId);
        });
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }

    @Test
    void testGetTransaction_WithNullId() throws ParseException {
        // Arrange
        String transactionId = null;
        when(transactionsRepository.findTransaction(null)).thenReturn(null);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction(null);
    }

    @Test
    void testGetTransaction_WithEmptyId() throws ParseException {
        // Arrange
        String transactionId = "";
        when(transactionsRepository.findTransaction("")).thenReturn(null);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction("");
    }

    @Test
    void testGetTransaction_WithPagadoState() throws ParseException {
        // Arrange
        String transactionId = "456";
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId(transactionId);
        expectedTransaction.setNombre("Gasolina");
        expectedTransaction.setFecha(Date.valueOf("2024-01-16"));
        expectedTransaction.setValor(75.25);
        expectedTransaction.setEstado(Transaction.Estado.Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(expectedTransaction);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(Transaction.Estado.Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }

    @Test
    void testGetTransaction_WithSpecialCharactersInId() throws ParseException {
        // Arrange
        String transactionId = "trans-123_456";
        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId(transactionId);
        expectedTransaction.setNombre("Test Transaction");

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(expectedTransaction);

        // Act
        Transaction result = getTransactionsUseCase.getTransaction(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }
}
