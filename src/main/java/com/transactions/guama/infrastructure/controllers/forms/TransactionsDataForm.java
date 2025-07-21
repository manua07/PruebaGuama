package com.transactions.guama.infrastructure.controllers.forms;

import java.sql.Date;

import com.transactions.guama.domain.Transaction;

import jakarta.validation.constraints.NotBlank;
import software.amazon.awssdk.annotations.NotNull;

public class TransactionsDataForm {

    @NotBlank
    private String id;

    @NotBlank
    private String nombre;

    @NotNull
    private Date fecha;

    @NotNull
    private Double valor;

    @NotBlank
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
