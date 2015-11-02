package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        String concepto = "";
        String descripcion = "";
        int cant = 0;
        concepto = mConcepto.getText().toString();
        descripcion = mDesc.getText().toString();
        cant = Integer.parseInt(mCant.getText().toString());

        Gastos g = new Gastos();
        g.setCantidad(cant);
        g.setConcepto(concepto);
        g.setDescripcion(descripcion);

       /* Intent i = new Intent(this, GastosActivity.class);

        setResult(RESULT_OK, i);
        startActivity(i);*/
        Intent i= new Intent();		//creamos un nuevo intent
        i.putExtra("GASTO", g);
        setResult(RESULT_OK,i);	//ponemos como resultado "CANCELED"
        finish();
    }
}