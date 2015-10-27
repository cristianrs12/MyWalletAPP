package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        enterButton.setOnClickListener(new View.OnClickListener() {

            // Call enterClicked() when pressed

            @Override
            public void onClick(View v) {

                enterClicked();

            }
        });
    }

    private void enterClicked() {

        String concepto="";
        String descripcion="";
        int cant=0;
        concepto=mConcepto.getText().toString();
        descripcion=mDesc.getText().toString();
        cant=Integer.parseInt(mCant.getText().toString());

       /* Gastos g = new Gastos();
        g.setCantidad(cant);
        g.setConcepto(concepto);
        g.setDescripcion(descripcion);
        */
        Intent i = new Intent(this,GastosActivity.class);
        i.putExtra("Concepto", concepto);
        i.putExtra("Cantidad", cant);
        i.putExtra("Descripcion", descripcion);
        setResult(RESULT_OK, i);
        startActivity(i);
    }





}
