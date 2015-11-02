package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

public class AddGastosActivity extends AppCompatActivity {

    EditText mConcepto;
    EditText mDesc;
    EditText mCant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gastos);
        mConcepto = (EditText) findViewById(R.id.concepto);
        mCant = (EditText) findViewById(R.id.cantidad);
        mDesc = (EditText) findViewById(R.id.desc);

        Button enterButton = (Button) findViewById(R.id.add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        enterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                enterClicked();
            }
        });
    }

    private void enterClicked() {

        Gastos g = new Gastos();
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

            g.setCantidad(cant);
            g.setConcepto(concepto);
            g.setDescripcion(descripcion);

            //Creamos un nuevo intent
            Intent i= new Intent();
            i.putExtra("GASTO", g);

            //ponemos como resultado "SUCCEEDED", R_OK=-1
            setResult(RESULT_OK, i);
            finish();
        }
    }
}