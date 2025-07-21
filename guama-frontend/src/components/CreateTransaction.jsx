import React, { useState } from 'react';
import { createTransaction } from '../services/transactionService';
import { useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import Swal from 'sweetalert2';

const CreateTransaction = () => {
    const [form, setForm] = useState({
        nombre: '',
        fecha: '',
        valor: '',
        estado: 'No_Pagado',
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
    e.preventDefault();
    const [year, month, day] = form.fecha.split('-');
    const localDate = new Date(year, month - 1, day);

    const transactionWithId = {
        ...form,
        id: uuidv4(),
        fecha: localDate.toISOString(),
    };

        await createTransaction(transactionWithId);

        Swal.fire({
            text: 'Transacción creada correctamente.',
            icon: 'success',
            confirmButtonColor: '#0077cc',
            confirmButtonText: 'OK',
        }).then(() => {
            navigate('/');
        });
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>➕ Agregar Transacción</h2>
            <form onSubmit={handleSubmit} style={styles.form}>
                <label style={styles.label}>Nombre:</label>
                <input
                    type="text"
                    name="nombre"
                    value={form.nombre}
                    onChange={handleChange}
                    required
                    style={styles.input}
                />

                <label style={styles.label}>Fecha:</label>
                <input
                    type="date"
                    name="fecha"
                    value={form.fecha}
                    onChange={handleChange}
                    required
                    style={styles.input}
                />

                <label style={styles.label}>Valor:</label>
                <input
                    type="number"
                    name="valor"
                    value={form.valor}
                    onChange={handleChange}
                    required
                    style={styles.input}
                />

                <label style={styles.label}>Estado:</label>
                <select
                    name="estado"
                    value={form.estado}
                    onChange={handleChange}
                    required
                    style={styles.input}
                >
                    <option value="Pagado">Pagado</option>
                    <option value="No_Pagado">No pagado</option>
                </select>

                <button type="submit" style={styles.button}>Guardar</button>
                <button type="button" style={styles.cancelButton} onClick={() => navigate('/')}>Volver</button>
            </form>
        </div>
    );
};

const styles = {
    title: {
    textAlign: 'center',
    fontSize: '28px',
    fontWeight: 'bold',
    color: '#0077cc',
    marginBottom: '30px',
    },
    container: {
        padding: '40px',
        fontFamily: 'Arial, sans-serif',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        maxWidth: '400px',
        margin: '0 auto',
    },
    label: {
        marginTop: '10px',
        marginBottom: '5px',
    },
    input: {
        padding: '8px',
        borderRadius: '4px',
        border: '1px solid #ccc',
    },
    button: {
        marginTop: '20px',
        padding: '10px',
        backgroundColor: '#0077cc',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
    },
    cancelButton: {
        marginTop: '10px',
        padding: '10px',
        backgroundColor: '#aaa',
        color: 'white',
        border: 'none',
        borderRadius: '4px',
        cursor: 'pointer',
    },
};

export default CreateTransaction;
