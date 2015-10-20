package com.example.cristian.mywallet;

/**
 * Created by Cristian on 20/10/2015.
 */
public class Gastos {
    private String concepto;
    private String descripcion;
    private int cantidad;

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Gastos(){
        this.concepto="";
        this.descripcion="";
        this.cantidad=0;
    }

    String getConcepto(){
        return this.concepto;
    }
    String getDescripcion(){
        return this.descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

}
