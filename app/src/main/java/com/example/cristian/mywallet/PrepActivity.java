package com.example.cristian.mywallet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class PrepActivity extends Activity{

    private WalletDBAdapter dbAdapter;
    private int presu;

    TextView titulo;
    EditText prep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prep_activity);
        presu=0;
        titulo = (TextView) findViewById(R.id.titulo);
        prep = (EditText) findViewById(R.id.presupuesto);

        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        Button enterButton = (Button) findViewById(R.id.APrep);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(prep.getText().toString())) {
                    prep.setError("Cantidad obligatoria");
                }else{
                    presu = Integer.parseInt(prep.getText().toString());
                    ContentValues reg = new ContentValues();
                    reg.put(WalletDBAdapter.C_PRESUPUESTO,presu);
                    dbAdapter.insert(reg);
                    Toast.makeText(PrepActivity.this, "Nuevo Presupuesto Añadido", Toast.LENGTH_SHORT).show();

                    Intent i= new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });


    }


}
