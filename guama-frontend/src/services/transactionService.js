import axios from 'axios';

const API_URL = "http://localhost:8080/api/transactions";
const API_URL_PAYMENTS = "http://localhost:8080/api/payments";

export const getAllTransactions = async () => {
    const response = await axios.get(API_URL);
    return response.data;
};

export const createTransaction = async (transaction) => {
    const response = await axios.post(API_URL, transaction);
    return response.data;
};

export const getTransactionById = async (id) => {
    const response = await axios.get(`${API_URL}?id=${id}`);
    return response.data;
};

export const updateTransaction = async (id, transaction) => {
    try {
        const response = await axios.put(`${API_URL}?id=${id}`, transaction);
        return response.data;
    } catch (error) {
        console.error('Error updating transaction:', error);
        throw error;
    }
};

export const deleteTransaction = async (id) => {
    const response = await axios.delete(`${API_URL}?id=${id}`);
    return response.data;
};

export const payTransactions = async (valor) => {
    const response = await axios.post(`${API_URL_PAYMENTS}?valor=${valor}`);
    return response.data;
};

