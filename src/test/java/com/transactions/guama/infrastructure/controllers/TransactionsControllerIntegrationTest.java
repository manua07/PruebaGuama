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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transactions.guama.infrastructure.controllers.forms.TransactionsDataForm;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class TransactionsControllerIntegrationTest {

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
    void testCreateTransaction() throws Exception {
        // Given
        TransactionsDataForm transactionForm = new TransactionsDataForm();
        transactionForm.setId("TEST-001");
        transactionForm.setNombre("Test Transaction");
        transactionForm.setFecha(Date.valueOf(LocalDate.now()));
        transactionForm.setValor(100.50);
        transactionForm.setEstado("No_Pagado");

        String requestBody = objectMapper.writeValueAsString(transactionForm);

        // When & Then
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value("TEST-001"))
                .andExpect(jsonPath("$.nombre").value("Test Transaction"))
                .andExpect(jsonPath("$.valor").value(100.50))
                .andExpect(jsonPath("$.estado").value("No_Pagado"));
    }

    @Test
    void testGetAllTransactions() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/transactions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetTransactionById() throws Exception {
        // Given
        String transactionId = "TEST-001";

        // When & Then
        mockMvc.perform(get("/api/transactions")
                .param("id", transactionId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(transactionId));
    }

    @Test
    void testGetTransactionByIdNotFound() throws Exception {
        // Given
        String nonExistentId = "NON-EXISTENT";

        // When & Then
        mockMvc.perform(get("/api/transactions")
                .param("id", nonExistentId))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        // Given
        TransactionsDataForm transactionForm = new TransactionsDataForm();
        transactionForm.setId("TEST-001");
        transactionForm.setNombre("Updated Transaction");
        transactionForm.setFecha(Date.valueOf(LocalDate.now()));
        transactionForm.setValor(200.75);
        transactionForm.setEstado("No_Pagado");

        String requestBody = objectMapper.writeValueAsString(transactionForm);

        // When & Then
        mockMvc.perform(put("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("TEST-001"))
                .andExpect(jsonPath("$.nombre").value("Updated Transaction"))
                .andExpect(jsonPath("$.valor").value(200.75))
                .andExpect(jsonPath("$.estado").value("No_Pagado"));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        // Given
        String transactionId = "TEST-001";

        // When & Then
        mockMvc.perform(delete("/api/transactions")
            .param("id", transactionId))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void testDeleteTransactionNotFound() throws Exception {
        // Given
        String nonExistentId = "NON-EXISTENT";

        // When & Then
        mockMvc.perform(delete("/api/transactions")
                .param("id", nonExistentId))
                .andDo(print())
                .andExpect(content().string(""));
    }

    @Test
    void testCreateTransactionWithInvalidData() throws Exception {
        // Given - Transaction without required fields
        TransactionsDataForm transactionForm = new TransactionsDataForm();
        transactionForm.setValor(0.0);

        String requestBody = objectMapper.writeValueAsString(transactionForm);

        // When & Then
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCorsHeaders() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/transactions")
                .header("Origin", "http://localhost:3000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }
} 