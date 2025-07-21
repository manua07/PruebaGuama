import React, { useEffect, useState } from 'react';
import { getAllTransactions, deleteTransaction } from '../services/transactionService';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';

const TransactionList = () => {
    const [transactions, setTransactions] = useState([]);
    const navigate = useNavigate();
    const [filtered, setFiltered] = useState([]);
    const [filters, setFilters] = useState({
        nombre: '',
        fecha: '',
        estado: '',
    });

    useEffect(() => {
        const fetchData = async () => {
        const data = await getAllTransactions();
        setTransactions(data);
        };
        fetchData();
    }, []);

    useEffect(() => {
        let filteredData = [...transactions];

        if (filters.nombre) {
            filteredData = filteredData.filter(tx =>
                tx.nombre.toLowerCase().includes(filters.nombre.toLowerCase())
            );
        }

        if (filters.fecha) {
            filteredData = filteredData.filter(tx => tx.fecha === filters.fecha);
        }

        if (filters.estado) {
            filteredData = filteredData.filter(tx => tx.estado === filters.estado);
        }

        setFiltered(filteredData);
    }, [filters, transactions]);

    const handleAgregar = () => {
        navigate('/agregar');
    };

    const handleChange = (e) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleDelete = async (id, estado) => {
    if (estado === 'Pagado') {
        Swal.fire({
            icon: 'warning',
            title: 'Acción no permitida',
            text: 'No se puede eliminar una transacción que ya está pagada.',
            confirmButtonColor: '#0077cc',
        });
        return;
    }

    const confirmDelete = Swal.fire({
            icon: 'success',
            text: 'Registro eliminado correctamente',
            confirmButtonColor: '#0077cc',
        });
    if (confirmDelete) {
        await deleteTransaction(id);
        setTransactions(transactions.filter(tx => tx.id !== id));
    }
    };

    return (
        <div style={styles.container}>
        <div style={styles.filtersContainer}>
                <input
                    type="text"
                    placeholder="Filtrar por nombre"
                    name="nombre"
                    value={filters.nombre}
                    onChange={handleChange}
                    style={styles.filterInput}
                />
                <input
                    type="date"
                    name="fecha"
                    value={filters.fecha}
                    onChange={handleChange}
                    style={styles.filterInput}
                />
                <select
                    name="estado"
                    value={filters.estado}
                    onChange={handleChange}
                    style={styles.filterInput}
                >
                    <option value="">Todos</option>
                    <option value="Pagado">Pagado</option>
                    <option value="No_Pagado">No pagado</option>
                </select>
            </div>
                <button style={styles.addButton} onClick={handleAgregar}>Agregar transaccion</button>
                <button style={styles.payButton}>Pagar transacciones</button>
        <table style={styles.table}>
            <thead>
            <tr>
                <th style={{ ...styles.th, width: '300px', textAlign: 'center' }}>ID</th>
                <th style={styles.th}>Nombre</th>
                <th style={styles.th}>Fecha</th>
                <th style={styles.th}>Valor</th>
                <th style={styles.th}>Estado</th>
                <th style={{ ...styles.th, width: '150px', textAlign: 'center' }}>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <table style={styles.table}></table>
            {filtered.length > 0 ? (
                filtered.map((tx, index) => (
                    <tr key={index} style={index % 2 === 0 ? styles.evenRow : styles.oddRow}>
                        <td style={styles.td}>{tx.id}</td>
                        <td style={styles.td}>{tx.nombre}</td>
                        <td style={styles.td}>{tx.fecha}</td>
                        <td style={styles.td}>${parseFloat(tx.valor).toLocaleString()}</td>
                        <td style={styles.td}>{tx.estado}</td>
                        <td style={{ ...styles.td, textAlign: 'center' }}>
                            <div style={styles.actionButtons}>
                                <button style={styles.editButton} onClick={() => navigate(`/edit/${tx.id}`)}>
                                    Editar
                                </button>
                                <button style={styles.deleteButton} onClick={() => handleDelete(tx.id, tx.estado)}>
                                    Eliminar
                                </button>
                            </div>
                        </td>
                    </tr>
                ))
                ) : (
                    <tr>
                        <td colSpan="6" style={{ textAlign: 'center', padding: '20px' }}>
                            No se encontraron transacciones.
                        </td>
                    </tr>
                )}
            </tbody>
        </table>
        </div>
    );
    };

    const styles = {
    container: {
        padding: '40px',
        fontFamily: 'Arial, sans-serif',
        backgroundColor: '#f7f9fb',
        minHeight: '100vh',
    },
    title: {
        textAlign: 'center',
        marginBottom: '30px',
        color: '#333',
    },
    table: {
        width: '100%',
        borderCollapse: 'collapse',
        backgroundColor: '#fff',
        boxShadow: '0 0 8px rgba(0, 0, 0, 0.1)',
    },
    th: {
        padding: '12px',
        backgroundColor: '#0077cc',
        color: 'white',
        textAlign: 'left',
        fontWeight: 'bold',
    },
    td: {
        padding: '12px',
        borderBottom: '1px solid #ddd',
    },
    evenRow: {
        backgroundColor: '#f2f7fc',
    },
    oddRow: {
        backgroundColor: '#ffffff',
    },
    actionButtons: {
        display: 'flex',
        gap: '20px',
    },

    editButton: {
        backgroundColor: '#0077cc',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
    },

    deleteButton: {
        backgroundColor: '#dc3545',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
    },

    addButton: { 
        marginRight: '10px',
        backgroundColor: '#0077cc',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
        justifyContent: 'flex-end',
        marginBottom: '30px',
        
    },
    payButton: {
        backgroundColor: '#28863bff',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
        justifyContent: 'flex-end',
    },
    filterInput: {
        padding: '8px',
        border: '1px solid #ccc',
        borderRadius: '4px',
        fontSize: '14px',
        minWidth: '180px',
    },
    filtersContainer: {
        display: 'flex',
        justifyContent: 'flex-end',
        gap: '10px',
        flexWrap: 'wrap',
    },

    };

export default TransactionList;
