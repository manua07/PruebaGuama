package com.transactions.guama.infrastructure.controllers.forms;

import com.transactions.guama.domain.Transaction;

public class TransactionsDataForm {

    private String id;
    private String nombre;
    private String fecha;
    private Number valor;
    private String estado;

    public Transaction toTransactions() {
        return new Transaction(id, nombre, fecha, valor, estado);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getFecha() {
        return fecha;
    }


    public void setFecha(String fecha) {
        this.fecha = fecha;
    }


    public Number getValor() {
        return valor;
    }


    public void setValor(Number valor) {
        this.valor = valor;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }

}
