package com.transactions.guama.infrastructure.controllers.forms;

import java.sql.Date;

import com.transactions.guama.domain.Transaction;

public class TransactionsDataForm {

    private String id;
    private String nombre;
    private Date fecha;
    private double valor;
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


    public Date getFecha() {
        return fecha;
    }


    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public double getValor() {
        return valor;
    }


    public void setValor(double valor) {
        this.valor = valor;
    }


    public String getEstado() {
        return estado;
    }


    public void setEstado(String estado) {
        this.estado = estado;
    }

}
