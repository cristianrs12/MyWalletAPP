package com.example.cristian.mywallet;

import java.io.Serializable;

/**
 * Created by Cristian on 20/10/2015.
 */
public class Gastos implements Serializable{
    private String concepto;
    private String descripcion;
    private Float cantidad;

    public Gastos(){
        this.concepto="";
        this.descripcion="";
        this.cantidad= (float) 0.00;
    }

    public String getConcepto() {
        return this.concepto;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public float getCantidad() { return  this.cantidad; }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }
}
