package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModificarPrep extends AppCompatActivity {

    private WalletDBAdapter dbAdapter;
    private double presupuesto, gastado;
    TextView titulo;
    EditText prep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prep_activity);
        presupuesto = 0;
        gastado = 0;
        titulo = (TextView) findViewById(R.id.titulo);
        prep = (EditText) findViewById(R.id.presupuesto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        if(Constants.disponible>0) {
            gastado = Constants.presupuesto - Constants.disponible;
        }else{
            if(Constants.disponible<0){
                gastado = 0-Constants.disponible;
                gastado = gastado + Constants.presupuesto;
            }else{
                gastado = Constants.presupuesto;
            }

        }
        Button enterButton = (Button) findViewById(R.id.APrep);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterClicked();
            }
        });
    }
    private void enterClicked(){
        if (TextUtils.isEmpty(prep.getText().toString())) {
            prep.setError("Cantidad obligatoria");
        }else{
            //presupuesto = Double.parseDouble(prep.getText().toString());

            Constants.presupuesto = Double.parseDouble(prep.getText().toString());
            if(gastado>0) {
                Constants.disponible = Constants.presupuesto - gastado;
            }else{
                if(gastado<0){
                    Constants.disponible = Constants.presupuesto + Constants.disponible;
                }else{
                    Constants.disponible = Constants.presupuesto;
                }

            }
            //Constants.disponible = Constants.presupuesto;
            Toast.makeText(ModificarPrep.this, "Nuevo Presupuesto Añadido", Toast.LENGTH_SHORT).show();

            ContentValues reg = new ContentValues();
            reg.put(WalletDBAdapter.C_ID,1);
            reg.put(WalletDBAdapter.C_DISPONIBLE,Constants.disponible);
            reg.put(WalletDBAdapter.C_PRESUPUESTO, Constants.presupuesto);
            dbAdapter.updatePrep(reg);

            Intent i= new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity(RESULT_OK);
    }
}

