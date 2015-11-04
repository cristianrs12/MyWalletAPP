package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

public class AddGastosActivity extends AppCompatActivity {

    EditText mConcepto;
    EditText mDesc;
    EditText mCant;

    private WalletDBAdapter dbAdapter;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gastos);
        mConcepto = (EditText) findViewById(R.id.concepto);
        mCant = (EditText) findViewById(R.id.cantidad);
        mDesc = (EditText) findViewById(R.id.desc);

        Button enterButton = (Button) findViewById(R.id.add);
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

    private void enterClicked() {

        String concepto, descripcion;
        int cant;

        concepto = mConcepto.getText().toString();
        descripcion = mDesc.getText().toString();

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
            cant = Integer.parseInt(mCant.getText().toString());

            // Añadimos los datos del formulario
            ContentValues reg = new ContentValues();
            reg.put(WalletDBAdapter.C_CONCEPTO, concepto);
            reg.put(WalletDBAdapter.C_DESCRIPCION, descripcion);
            reg.put(WalletDBAdapter.C_CANTIDAD, cant);
            reg.put(WalletDBAdapter.C_LOCALIZACION, "");

            dbAdapter.insert(reg);
            Toast.makeText(AddGastosActivity.this, "Gasto añadido correctamente", Toast.LENGTH_SHORT).show();

            Intent i= new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }
}