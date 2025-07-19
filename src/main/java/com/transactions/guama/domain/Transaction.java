package com.transactions.guama.domain;

import java.sql.Date;

import lombok.Data;

@Data
public class Transaction {

    private String id;
    private String nombre;
    private Date fecha;
    private double valor;
    private Estado estado;

    public Transaction(String id, String nombre, Date fecha, double valor, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.valor = valor;
        this.estado = Estado.valueOf(estado);
    }

    public Transaction() {
    }

    public enum Estado {
    Pagado ,
    No_Pagado ,
    }

}
