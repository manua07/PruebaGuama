import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getTransactionById, updateTransaction } from '../services/transactionService';
import Swal from 'sweetalert2';


const EditTransaction = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    
    const [form, setForm] = useState({
        nombre: '',
        fecha: '',
        valor: '',
        estado: 'No_Pagado',
    });

    useEffect(() => {
    const fetchTransaction = async () => {
        try {
            const data = await getTransactionById(id);
            if (data) {
                if (data.estado === 'Pagado') {
                    Swal.fire({
                        icon: 'warning',
                        text: 'No se puede editar una transacción que ya está pagada.',
                        confirmButtonColor: '#0077cc',
                        }).then(() => {
                            navigate('/');
                        });
                    }
                else {
                setForm({
                    id: data.id,
                    nombre: data.nombre || '',
                    fecha: data.fecha || '',
                    valor: data.valor || '',
                    estado: data.estado || 'No_Pagado',
                });
            }}
            } catch (error) {
                console.error('Error al obtener la transacción:', error);
                alert('No se pudo cargar la transacción.');
            }
        };

        if (id) {
            fetchTransaction();
        } else {
            alert('ID de transacción no válido.');
            navigate('/');
        }
    }, [id, navigate]);

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
    e.preventDefault();

    try {
        const [year, month, day] = form.fecha.split('-');
        const localDate = new Date(year, month - 1, day);

        const updatedForm = {
            ...form,
            fecha: localDate.toISOString(),
        };
        await updateTransaction(id, updatedForm);

        await Swal.fire({
            icon: 'success',
            text: 'Transacción actualizada correctamente',
            confirmButtonColor: '#0077cc',
        });

        navigate('/');
    } catch (error) {
        console.error('Error actualizando la transacción:', error);
        alert('Hubo un error actualizando la transacción.');
    }
};

    return (
        <div style={styles.container}>
            <h2 style={styles.title}> Editar Transacción</h2>
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

                <button type="submit" style={styles.button}>Actualizar</button>
                <button type="button" style={styles.cancelButton} onClick={() => navigate('/')}>Cancelar</button>
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

export default EditTransaction;
