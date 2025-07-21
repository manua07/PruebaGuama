import React, { useEffect, useState } from 'react';
import { getAllTransactions, deleteTransaction, payTransactions} from '../services/transactionService';
import { useNavigate } from 'react-router-dom';
import Swal from 'sweetalert2';

const TransactionList = () => {
    const [transactions, setTransactions] = useState([]);
    const [filtered, setFiltered] = useState([]);
    const [filters, setFilters] = useState({ nombre: '', fecha: '', estado: '' });
    const [modalOpen, setModalOpen] = useState(false);
    const [valorPago, setValorPago] = useState('');
    const [valorTotal, setValorTotal] = useState(0);
    const navigate = useNavigate();

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

    const handleAgregar = () => navigate('/agregar');

    const handleChange = (e) => {
        setFilters({ ...filters, [e.target.name]: e.target.value });
    };

    const handleDelete = async (id, estado) => {
        if (estado === 'Pagado') {
            Swal.fire({
                icon: 'warning',
                text: 'No se puede eliminar una transacción que ya está pagada.',
                confirmButtonColor: '#0077cc',
            });
            return;
        }

        await deleteTransaction(id);
        setTransactions(transactions.filter(tx => tx.id !== id));

        Swal.fire({
            icon: 'success',
            text: 'Transación eliminada correctamente',
            confirmButtonColor: '#0077cc',
        });
    };

    const handleOpenModal = () => {
        const total = transactions
            .filter(tx => tx.estado === 'No_Pagado')
            .reduce((sum, tx) => sum + parseFloat(tx.valor), 0);
        setValorTotal(total);
        setValorPago('');
        setModalOpen(true);
    };

    const handleCloseModal = () => {
        setModalOpen(false);
    };

    const handleConfirmPago = async () => {
        const valor = parseFloat(valorPago);
        if (isNaN(valor) || valor <= 0) {
            Swal.fire('Error', 'Ingresa un valor válido', 'error');
            return;
        }

        if (valor < valorTotal) {
            Swal.fire('Error', 'El valor ingresado no cubre el total a pagar', 'error');
            return;
        }

        await payTransactions(valor);

            Swal.fire({
                icon: 'success',
                text: `Transacciones pagadas correctamente`,
                confirmButtonColor: '#0077cc',
            });

            setModalOpen(false);

            const updatedTransactions = await getAllTransactions();
            setTransactions(updatedTransactions);
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

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginBottom: '20px' }}>
                <button style={styles.addButton} onClick={handleAgregar}>Agregar transacción</button>
                <button style={styles.payButton} onClick={handleOpenModal}>Pagar transacciones</button>
            </div>

            <table style={styles.table}>
                <thead>
                    <tr>
                        <th style={{ ...styles.th, width: '400px', textAlign: 'center' }}>ID</th>
                        <th style={styles.th}>Nombre</th>
                        <th style={styles.th}>Fecha</th>
                        <th style={styles.th}>Valor</th>
                        <th style={styles.th}>Estado</th>
                        <th style={{ ...styles.th, width: '150px', textAlign: 'center' }}>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {filtered.length > 0 ? (
                        filtered.map((tx, index) => (
                            <tr key={index} style={index % 2 === 0 ? styles.evenRow : styles.oddRow}>
                                <td style={styles.td}>{tx.id}</td>
                                <td style={styles.td}>{tx.nombre}</td>
                                <td style={styles.td}>{tx.fecha}</td>
                                <td style={styles.td}>${parseFloat(tx.valor).toLocaleString()}</td>
                                <td style={styles.td}>
                                    <span
                                        style={{
                                            padding: '4px 8px',
                                            borderRadius: '12px',
                                            color: 'white',
                                            backgroundColor: tx.estado === 'Pagado' ? '#28a745' : '#dc3545',
                                            fontSize: '12px',
                                            fontWeight: 'bold',
                                        }}
                                    >
                                        {tx.estado === 'Pagado' ? 'Pagado' : 'No pagado'}
                                    </span>
                                </td>
                                <td style={{ ...styles.td, textAlign: 'center' }}>
                                    <div style={styles.actionButtons}>
                                        <button style={styles.editButton} onClick={() => navigate(`/edit/${tx.id}`)}>Editar</button>
                                        <button style={styles.deleteButton} onClick={() => handleDelete(tx.id, tx.estado)}>Eliminar</button>
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

            {modalOpen && (
                <div style={styles.modalOverlay}>
                    <div style={styles.modal}>
                        <h3>Valor total a pagar: <strong>${valorTotal.toLocaleString()}</strong></h3>
                        <input
                            type="number"
                            placeholder="Ingrese el valor a pagar"
                            value={valorPago}
                            onChange={(e) => setValorPago(e.target.value)}
                            style={styles.modalInput}
                        />
                        <div style={{ marginTop: '20px', display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
                            <button onClick={handleCloseModal} style={styles.cancelButton}>Cancelar</button>
                            <button onClick={handleConfirmPago} style={styles.confirmButton}>Confirmar pago</button>
                        </div>
                    </div>
                </div>
            )}
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
        backgroundColor: '#0077cc',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
    },
    payButton: {
        backgroundColor: '#28863bff',
        color: 'white',
        border: 'none',
        padding: '6px 12px',
        borderRadius: '4px',
        cursor: 'pointer',
    },
    filterInput: {
        padding: '8px',
        border: '1px solid #ccc',
        borderRadius: '4px',
        fontSize: '14px',
        minWidth: '180px',
    },
    modalOverlay: {
        position: 'fixed',
        top: 0, left: 0, right: 0, bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
    },
    modal: {
        backgroundColor: 'white',
        padding: '30px',
        borderRadius: '8px',
        width: '400px',
        boxShadow: '0 0 10px rgba(0, 0, 0, 0.2)',
    },
    modalInput: {
        width: '100%',
        padding: '10px',
        marginTop: '10px',
        borderRadius: '4px',
        border: '1px solid #ccc',
        fontSize: '16px',
    },
    cancelButton: {
        backgroundColor: '#ccc',
        color: '#000',
        border: 'none',
        padding: '8px 16px',
        borderRadius: '4px',
        cursor: 'pointer',
    },
    confirmButton: {
        backgroundColor: '#0077cc',
        color: 'white',
        border: 'none',
        padding: '8px 16px',
        borderRadius: '4px',
        cursor: 'pointer',
    },
};

export default TransactionList;
