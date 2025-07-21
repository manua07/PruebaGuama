package com.transactions.guama.infrastructure.controllers;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
class PaymentsControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testPaymentTransactionWithValidAmount() throws Exception {
        // Given
        double paymentAmount = 100.50;

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", String.valueOf(paymentAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPaymentTransactionWithZeroAmount() throws Exception {
        // Given
        double paymentAmount = 0.0;

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", String.valueOf(paymentAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPaymentTransactionWithNegativeAmount() throws Exception {
        // Given
        double paymentAmount = -50.0;

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", String.valueOf(paymentAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPaymentTransactionWithLargeAmount() throws Exception {
        // Given
        double paymentAmount = 999999.99;

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", String.valueOf(paymentAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPaymentTransactionWithoutValorParameter() throws Exception {
        // When & Then - Should fail because valor parameter is required
        mockMvc.perform(post("/api/payments"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPaymentTransactionWithInvalidValorParameter() throws Exception {
        // Given
        String invalidValor = "invalid_number";

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", invalidValor))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPaymentTransactionWithDecimalAmount() throws Exception {
        // Given
        double paymentAmount = 123.45;

        // When & Then
        mockMvc.perform(post("/api/payments")
                .param("valor", String.valueOf(paymentAmount)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testPaymentTransactionMethodNotAllowed() throws Exception {
        // Test that GET method is not allowed
        mockMvc.perform(get("/api/payments")
                .param("valor", "100.0"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());

        // Test that PUT method is not allowed
        mockMvc.perform(put("/api/payments")
                .param("valor", "100.0"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());

        // Test that DELETE method is not allowed
        mockMvc.perform(delete("/api/payments")
                .param("valor", "100.0"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }
} 