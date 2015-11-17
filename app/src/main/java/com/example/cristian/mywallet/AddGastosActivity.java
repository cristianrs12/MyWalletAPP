package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddGastosActivity extends AppCompatActivity {

    EditText mConcepto;
    EditText mDesc;
    EditText mCant;

    double presDisponible, presTotal;
    private Cursor cursorPres;

    private WalletDBAdapter dbAdapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gastos);
        mConcepto = (EditText) findViewById(R.id.concepto);
        mCant = (EditText) findViewById(R.id.cantidad);
        mDesc = (EditText) findViewById(R.id.desc);
        spinner = (Spinner) findViewById(R.id.spinner);
        Button enterButton = (Button) findViewById(R.id.add);
        DatosPorDefecto();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Creamos el adaptador
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        // Definimos la accion para el boton
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterClicked();
            }
        });
    }

    private void DatosPorDefecto() {
        List lista = new ArrayList<String>();
        lista.add("Comida");
        lista.add("Lujos");
        lista.add("Básicos");
        lista.add("Caprichos");
        lista.add("Mensual");
        lista.add("Transporte");
        lista.add("Otros");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
    }

    private void enterClicked() {

        String categoria, concepto, descripcion;
        double cant, newPres;

        concepto = mConcepto.getText().toString();
        descripcion = mDesc.getText().toString();

        this.cursorPres = dbAdapter.getRegistroPres(0);
        presDisponible = cursorPres.getDouble(cursorPres.getColumnIndex(WalletDBAdapter.C_DISPONIBLE));
        presTotal = cursorPres.getDouble(cursorPres.getColumnIndex(WalletDBAdapter.C_PRESUPUESTO));
        
        //Comprueba que el campo "Concepto" no esté vacio
        if(concepto.isEmpty()){
            mConcepto.setError("Concepto obligatorio");
        //Comprueba que el campo "Descripcion" no esté vacio
        } else if (descripcion.isEmpty()) {
            mDesc.setError("Descripción obligatoria");
        //Comprueba que el campo "Cantidad" no esté vacio
        } else if(TextUtils.isEmpty(mCant.getText().toString())) {
            mCant.setError("Cantidad obligatoria");
        } else {
            cant = Double.parseDouble(mCant.getText().toString());
            categoria = spinner.getSelectedItem().toString();

            // Añadimos los datos del formulario
            ContentValues reg = new ContentValues();
            reg.put(WalletDBAdapter.C_CONCEPTO, concepto);
            reg.put(WalletDBAdapter.C_DESCRIPCION, descripcion);
            reg.put(WalletDBAdapter.C_CANTIDAD, cant);
            reg.put(WalletDBAdapter.C_LOCALIZACION, "");
            reg.put(WalletDBAdapter.C_CATEGORIA,categoria);

            dbAdapter.insert(reg);
            Toast.makeText(AddGastosActivity.this, "Gasto añadido correctamente", Toast.LENGTH_SHORT).show();

            //Actualizamos el presupuesto disponible
            ContentValues regPres = new ContentValues();
            newPres = presDisponible - cant;

            regPres.put(WalletDBAdapter.C_ID, 0);
            regPres.put(WalletDBAdapter.C_PRESUPUESTO, presTotal);
            regPres.put(WalletDBAdapter.C_DISPONIBLE, newPres);

            dbAdapter.updatePres(regPres);

            Toast.makeText(AddGastosActivity.this, "Presupuesto modificado", Toast.LENGTH_SHORT).show();

            Intent i= new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }
}