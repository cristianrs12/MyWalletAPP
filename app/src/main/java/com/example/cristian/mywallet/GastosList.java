package com.example.cristian.mywallet;

import android.util.Log;

import com.example.cristian.mywallet.Gastos;
import java.util.ArrayList;

/**
 * Created by Cristian on 20/10/2015.
 */
public class GastosList {
    public ArrayList<Gastos> ListaGastos;

    public GastosList() {
        this.ListaGastos = new ArrayList<>();

        Gastos gasto1 = new Gastos();
        Gastos gasto2 = new Gastos();
        Gastos gasto3 = new Gastos();
        Gastos gasto4 = new Gastos();
        Gastos gasto5 = new Gastos();

        gasto1.setConcepto("Prueba 1");
        gasto1.setDescripcion("Prueba de gasto 1");
        gasto1.setCantidad((float) 99.9);
        this.ListaGastos.add(gasto1);

        gasto2.setConcepto("Prueba 2");
        gasto2.setDescripcion("Prueba de gasto 2");
        gasto2.setCantidad((float)85.30);
        this.ListaGastos.add(gasto2);

        gasto3.setConcepto("Prueba 3");
        gasto3.setDescripcion("Prueba de gasto 3");
        gasto3.setCantidad((float)100);
        this.ListaGastos.add(gasto3);

        gasto4.setConcepto("Prueba 4");
        gasto4.setDescripcion("Prueba de gasto 4");
        gasto4.setCantidad((float)30.54);
        this.ListaGastos.add(gasto4);

        gasto5.setConcepto("Prueba 5");
        gasto5.setDescripcion("Prueba de gasto 5");
        gasto5.setCantidad((float)10);
        this.ListaGastos.add(gasto5);
    }

    public ArrayList<String> getListaGastos() {
        ArrayList<String> lista = new ArrayList<>();
        for (Gastos gasto:ListaGastos) {
            lista.add("Concepto: " + gasto.getConcepto() + "\n" +
                      "Desc: " + gasto.getDescripcion() + "\n" +
                      "Cantidad: " + gasto.getCantidad());
        }
        return lista;
    }

    public void setListaGastos(ArrayList<Gastos> listaGastos) {
        ListaGastos = listaGastos;
    }

    //TODO: Añadir elementos a la lista
    public void addElementoLista(){

    }
}
