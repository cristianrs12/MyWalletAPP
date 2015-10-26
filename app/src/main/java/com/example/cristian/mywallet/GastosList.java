package com.example.cristian.mywallet;

import android.util.Log;

import com.example.cristian.mywallet.Gastos;
import java.util.ArrayList;

/**
 * Created by Cristian on 20/10/2015.
 */
public class GastosList {
    private static ArrayList<Gastos> ListaGastos;
    public GastosList() {
        this.ListaGastos = new ArrayList<>();
        this.ListaGastos.add(new Gastos("Registro gasto 1", "Gasto realizado en la prueba 1", (float) 99.90));
        this.ListaGastos.add(new Gastos("Registro gasto 1", "Gasto realizado en la prueba 1", (float) 80));
        this.ListaGastos.add(new Gastos("Registro gasto 1", "Gasto realizado en la prueba 1", (float) 35));
        this.ListaGastos.add(new Gastos("Registro gasto 1", "Gasto realizado en la prueba 1", (float) 122));
        this.ListaGastos.add(new Gastos("Registro gasto 1", "Gasto realizado en la prueba 1", (float) 65.30));
    }

    public ArrayList<String> getListaGastos() {
        ArrayList<String> lista = new ArrayList<>();
        for (Gastos gasto:ListaGastos) {
            lista.add("Concepto: " + gasto.getConcepto() + "\n" +
                      "Desc: " + gasto.getDescripcion() + "\n" +
                      "Cantidad: " + gasto.getCantidad());

            Log.d("LISTCONTENT","Concepto: " + gasto.getConcepto() + "\n" +
                                "Desc: " + gasto.getDescripcion() + "\n" +
                                "Cantidad: " + gasto.getCantidad());
        }

        return lista;
    }

    public void setListaGastos(ArrayList<Gastos> listaGastos) {
        ListaGastos = listaGastos;
    }
}
