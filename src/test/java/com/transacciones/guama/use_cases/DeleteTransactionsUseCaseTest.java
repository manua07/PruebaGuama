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
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;
import com.transactions.guama.use_cases.DeleteTransactionsUseCase;

@ExtendWith(MockitoExtension.class)
class DeleteTransactionsUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private DeleteTransactionsUseCase deleteTransactionsUseCase;

    @BeforeEach
    void setUp() {
        deleteTransactionsUseCase = new DeleteTransactionsUseCase(transactionsRepository);
    }

    @Test
    void testDeleteTransaction_Success() throws ParseException {
        // Arrange
        String transactionId = "123";
        Transaction transactionToDelete = new Transaction();
        transactionToDelete.setId(transactionId);
        transactionToDelete.setNombre("Compra Supermercado");
        transactionToDelete.setFecha(Date.valueOf("2024-01-15"));
        transactionToDelete.setValor(150.50);
        transactionToDelete.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(transactionToDelete);
        when(transactionsRepository.delete(transactionId)).thenReturn(transactionToDelete);

        // Act
        Transaction result = deleteTransactionsUseCase.delete(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals("Compra Supermercado", result.getNombre());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
        verify(transactionsRepository, times(1)).delete(transactionId);
    }

    @Test
    void testDeleteTransaction_TransactionNotFound() throws ParseException {
        // Arrange
        String transactionId = "999";
        when(transactionsRepository.findTransaction(transactionId)).thenReturn(null);

        // Act
        Transaction result = deleteTransactionsUseCase.delete(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
    }

    @Test
    void testDeleteTransaction_PagadoTransaction_ThrowsException() throws ParseException {
        // Arrange
        String transactionId = "456";
        Transaction pagadoTransaction = new Transaction();
        pagadoTransaction.setId(transactionId);
        pagadoTransaction.setNombre("Gasolina");
        pagadoTransaction.setEstado(Transaction.Estado.Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(pagadoTransaction);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deleteTransactionsUseCase.delete(transactionId);
        });
        
        assertEquals("No se puede eliminar una transacción que ya está pagada.", exception.getMessage());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
        verify(transactionsRepository, never()).delete(anyString());
    }

    @Test
    void testDeleteTransaction_RepositoryThrowsException() throws ParseException {
        // Arrange
        String transactionId = "123";
        when(transactionsRepository.findTransaction(transactionId))
            .thenThrow(new ParseException("Error parsing date", 0));

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            deleteTransactionsUseCase.delete(transactionId);
        });
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
        verify(transactionsRepository, never()).delete(anyString());
    }

    @Test
    void testDeleteTransaction_WithNullId() throws ParseException {
        // Arrange
        String transactionId = null;
        when(transactionsRepository.findTransaction(null)).thenReturn(null);

        // Act
        Transaction result = deleteTransactionsUseCase.delete(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction(null);
        verify(transactionsRepository, never()).delete(anyString());
    }

    @Test
    void testDeleteTransaction_WithEmptyId() throws ParseException {
        // Arrange
        String transactionId = "";
        when(transactionsRepository.findTransaction("")).thenReturn(null);

        // Act
        Transaction result = deleteTransactionsUseCase.delete(transactionId);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction("");
    }

    @Test
    void testDeleteTransaction_NoPagadoTransaction_Success() throws ParseException {
        // Arrange
        String transactionId = "789";
        Transaction noPagadoTransaction = new Transaction();
        noPagadoTransaction.setId(transactionId);
        noPagadoTransaction.setNombre("Restaurante");
        noPagadoTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(noPagadoTransaction);

        // Act
        Transaction result = deleteTransactionsUseCase.delete(transactionId);

        // Assert
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
        verify(transactionsRepository, times(1)).delete(transactionId);
    }

    @Test
    void testDeleteTransaction_DeleteMethodThrowsException() throws ParseException {
        // Arrange
        String transactionId = "123";
        Transaction transactionToDelete = new Transaction();
        transactionToDelete.setId(transactionId);
        transactionToDelete.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction(transactionId)).thenReturn(transactionToDelete);
        doThrow(new RuntimeException("Database error")).when(transactionsRepository).delete(transactionId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            deleteTransactionsUseCase.delete(transactionId);
        });
        
        verify(transactionsRepository, times(1)).findTransaction(transactionId);
        verify(transactionsRepository, times(1)).delete(transactionId);
    }
} 
