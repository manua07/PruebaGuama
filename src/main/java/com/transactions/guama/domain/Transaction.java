package com.transactions.guama.domain;

import lombok.Data;

@Data
public class Transaction {

    private String id;
    private String nombre;
    private String fecha;
    private Number valor;
    private String estado;

    public Transaction(String id, String nombre, String fecha, Number valor, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.valor = valor;
        this.estado = estado;
    }

    public Transaction() {
    }

}
