package com.transactions.guama.infrastructure.controllers;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactions.guama.infrastructure.controllers.forms.TransactionsDataForm;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class CompleteWorkflowIntegrationTest {

        @Autowired
        private WebApplicationContext webApplicationContext;

        @Autowired
        private ObjectMapper objectMapper;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        }

        @Test
        void testCompleteTransactionWorkflow() throws Exception {
                // Step 1: Create multiple transactions
                String transactionId1 = "WORKFLOW-001";
                String transactionId2 = "WORKFLOW-002";
                String transactionId3 = "WORKFLOW-003";

                // Create first transaction
                TransactionsDataForm transaction1 = createTransactionForm(transactionId1, "Transaction 1", 100.0, "No_Pagado");
                mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction1)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId1))
                        .andExpect(jsonPath("$.estado").value("No_Pagado"));

                // Create second transaction
                TransactionsDataForm transaction2 = createTransactionForm(transactionId2, "Transaction 2", 200.0, "No_Pagado");
                mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction2)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId2))
                        .andExpect(jsonPath("$.estado").value("No_Pagado"));

                // Create third transaction
                TransactionsDataForm transaction3 = createTransactionForm(transactionId3, "Transaction 3", 300.0, "No_Pagado");
                mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction3)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId3))
                        .andExpect(jsonPath("$.estado").value("No_Pagado"));

                // Step 2: Verify all transactions exist
                mockMvc.perform(get("/api/transactions"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId1 + "')]").exists())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId2 + "')]").exists())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId3 + "')]").exists());

                // Step 3: Get specific transaction
                mockMvc.perform(get("/api/transactions")
                        .param("id", transactionId1))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId1))
                        .andExpect(jsonPath("$.nombre").value("Transaction 1"))
                        .andExpect(jsonPath("$.valor").value(100.0));

                // Step 4: Update a transaction
                TransactionsDataForm updatedTransaction = createTransactionForm(transactionId1, "Updated Transaction 1", 150.0, "Pagado");
                mockMvc.perform(put("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTransaction)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId1))
                        .andExpect(jsonPath("$.nombre").value("Updated Transaction 1"))
                        .andExpect(jsonPath("$.valor").value(150.0))
                        .andExpect(jsonPath("$.estado").value("Pagado"));

                // Step 5: Make a payment
                double paymentAmount = 250.0;
                mockMvc.perform(post("/api/payments")
                        .param("valor", String.valueOf(paymentAmount)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());

                // Step 6: Verify payment affected transactions
                mockMvc.perform(get("/api/transactions"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());

                // Step 7: Delete a transaction
                mockMvc.perform(delete("/api/transactions")
                        .param("id", transactionId3))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(transactionId3));

                // Step 8: Verify transaction was deleted
                mockMvc.perform(get("/api/transactions"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId1 + "')]").exists())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId2 + "')]").exists())
                        .andExpect(jsonPath("$[?(@.id == '" + transactionId3 + "')]").doesNotExist());
        }

        @Test
        void testPaymentWorkflowWithDifferentAmounts() throws Exception {
                // Create test transactions
                String transactionId1 = "PAYMENT-001";
                String transactionId2 = "PAYMENT-002";

                TransactionsDataForm transaction1 = createTransactionForm(transactionId1, "Payment Test 1", 100.0, "No_Pagado");
                mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction1)))
                        .andExpect(status().isOk());

                TransactionsDataForm transaction2 = createTransactionForm(transactionId2, "Payment Test 2", 200.0, "No_Pagado");
                mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction2)))
                        .andExpect(status().isOk());

                // Test payment with exact amount
                mockMvc.perform(post("/api/payments")
                        .param("valor", "100.0"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());

                // Test payment with partial amount
                mockMvc.perform(post("/api/payments")
                        .param("valor", "50.0"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());

                // Test payment with amount larger than total
                mockMvc.perform(post("/api/payments")
                        .param("valor", "500.0"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray());
        }

        @Test
        void testErrorHandlingWorkflow() throws Exception {
                // Test getting non-existent transaction
                mockMvc.perform(get("/api/transactions")
                        .param("id", "NON-EXISTENT"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(""));

                // Test deleting non-existent transaction
                mockMvc.perform(delete("/api/transactions")
                        .param("id", "NON-EXISTENT"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(""));

                // Test updating non-existent transaction
                TransactionsDataForm nonExistentTransaction = createTransactionForm("NON-EXISTENT", "Non Existent", 100.0, "No_Pagado");
                mockMvc.perform(put("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nonExistentTransaction)))
                        .andExpect(content().string(""));
        }

        private TransactionsDataForm createTransactionForm(String id, String nombre, double valor, String estado) {
                TransactionsDataForm form = new TransactionsDataForm();
                form.setId(id);
                form.setNombre(nombre);
                form.setFecha(Date.valueOf(LocalDate.now()));
                form.setValor(valor);
                form.setEstado(estado);
                return form;
        }
}
