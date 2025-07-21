package com.transactions.guama.use_cases;

import java.sql.Date;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.transactions.guama.domain.Transaction;
import com.transactions.guama.domain.TransactionsRepositoryInterface;

@ExtendWith(MockitoExtension.class)
class EditTransactionsUseCaseTest {

    @Mock
    private TransactionsRepositoryInterface transactionsRepository;

    private EditTransactionsUseCase editTransactionsUseCase;

    @BeforeEach
    void setUp() {
        editTransactionsUseCase = new EditTransactionsUseCase(transactionsRepository);
    }

    @Test
    void testEditTransaction_Success() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("123");
        transactionToEdit.setNombre("Compra Supermercado Actualizada");
        transactionToEdit.setFecha(Date.valueOf("2024-01-15"));
        transactionToEdit.setValor(200.00);
        transactionToEdit.setEstado(Transaction.Estado.No_Pagado);

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId("123");
        existingTransaction.setNombre("Compra Supermercado");
        existingTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction("123")).thenReturn(existingTransaction);
        when(transactionsRepository.save(transactionToEdit)).thenReturn(transactionToEdit);

        // Act
        Transaction result = editTransactionsUseCase.editTransaction(transactionToEdit);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("Compra Supermercado Actualizada", result.getNombre());
        assertEquals(200.00, result.getValor());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction("123");
        verify(transactionsRepository, times(1)).save(transactionToEdit);
    }

    @Test
    void testEditTransaction_TransactionNotFound() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("999");
        transactionToEdit.setNombre("Transacción Inexistente");

        when(transactionsRepository.findTransaction("999")).thenReturn(null);

        // Act
        Transaction result = editTransactionsUseCase.editTransaction(transactionToEdit);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction("999");
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testEditTransaction_PagadoTransaction_ThrowsException() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("456");
        transactionToEdit.setNombre("Gasolina Actualizada");

        Transaction existingPagadoTransaction = new Transaction();
        existingPagadoTransaction.setId("456");
        existingPagadoTransaction.setNombre("Gasolina");
        existingPagadoTransaction.setEstado(Transaction.Estado.Pagado);

        when(transactionsRepository.findTransaction("456")).thenReturn(existingPagadoTransaction);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            editTransactionsUseCase.editTransaction(transactionToEdit);
        });
        
        assertEquals("No se puede editar una transacción que ya está pagada.", exception.getMessage());
        
        verify(transactionsRepository, times(1)).findTransaction("456");
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testEditTransaction_RepositoryThrowsException() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("123");

        when(transactionsRepository.findTransaction("123"))
            .thenThrow(new ParseException("Error parsing date", 0));

        // Act & Assert
        assertThrows(ParseException.class, () -> {
            editTransactionsUseCase.editTransaction(transactionToEdit);
        });
        
        verify(transactionsRepository, times(1)).findTransaction("123");
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testEditTransaction_WithNullTransaction() throws ParseException {
        // Arrange
        Transaction transactionToEdit = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            editTransactionsUseCase.editTransaction(transactionToEdit);
        });
        
        verify(transactionsRepository, never()).findTransaction(anyString());
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testEditTransaction_NoPagadoTransaction_Success() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("789");
        transactionToEdit.setNombre("Restaurante Actualizado");
        transactionToEdit.setValor(60.00);
        transactionToEdit.setEstado(Transaction.Estado.No_Pagado);

        Transaction existingNoPagadoTransaction = new Transaction();
        existingNoPagadoTransaction.setId("789");
        existingNoPagadoTransaction.setNombre("Restaurante");
        existingNoPagadoTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction("789")).thenReturn(existingNoPagadoTransaction);
        when(transactionsRepository.save(transactionToEdit)).thenReturn(transactionToEdit);

        // Act
        Transaction result = editTransactionsUseCase.editTransaction(transactionToEdit);

        // Assert
        assertNotNull(result);
        assertEquals("789", result.getId());
        assertEquals("Restaurante Actualizado", result.getNombre());
        assertEquals(60.00, result.getValor());
        assertEquals(Transaction.Estado.No_Pagado, result.getEstado());
        
        verify(transactionsRepository, times(1)).findTransaction("789");
        verify(transactionsRepository, times(1)).save(transactionToEdit);
    }

    @Test
    void testEditTransaction_SaveMethodThrowsException() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("123");
        transactionToEdit.setNombre("Test Transaction");

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId("123");
        existingTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction("123")).thenReturn(existingTransaction);
        when(transactionsRepository.save(transactionToEdit))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            editTransactionsUseCase.editTransaction(transactionToEdit);
        });
        
        verify(transactionsRepository, times(1)).findTransaction("123");
        verify(transactionsRepository, times(1)).save(transactionToEdit);
    }

    @Test
    void testEditTransaction_WithNullId() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId(null);
        transactionToEdit.setNombre("Test Transaction");

        when(transactionsRepository.findTransaction(null)).thenReturn(null);

        // Act
        Transaction result = editTransactionsUseCase.editTransaction(transactionToEdit);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction(null);
        verify(transactionsRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testEditTransaction_RepositoryReturnsNullOnSave() throws ParseException {
        // Arrange
        Transaction transactionToEdit = new Transaction();
        transactionToEdit.setId("123");
        transactionToEdit.setNombre("Test Transaction");

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId("123");
        existingTransaction.setEstado(Transaction.Estado.No_Pagado);

        when(transactionsRepository.findTransaction("123")).thenReturn(existingTransaction);
        when(transactionsRepository.save(transactionToEdit)).thenReturn(null);

        // Act
        Transaction result = editTransactionsUseCase.editTransaction(transactionToEdit);

        // Assert
        assertNull(result);
        verify(transactionsRepository, times(1)).findTransaction("123");
        verify(transactionsRepository, times(1)).save(transactionToEdit);
    }
}
