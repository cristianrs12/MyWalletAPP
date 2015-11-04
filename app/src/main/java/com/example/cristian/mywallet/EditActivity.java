package com.example.cristian.mywallet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditActivity extends Activity {

    private WalletDBAdapter dbAdapter;
    private Cursor cursor;

    // Modo del formulario
    private int modo ;

    // Identificador del registro que se edita cuando la opción es MODIFICAR
    private long id ;

    // Elementos de la vista
    private EditText concepto, descripcion, cantidad, location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if (extra == null) return;

        // Obtenemos los elementos de la vista
        this.concepto = (EditText) findViewById(R.id.edit_concepto);
        this.descripcion = (EditText) findViewById(R.id.edit_desc);
        this.cantidad = (EditText) findViewById(R.id.edit_cantidad);
        this.location = (EditText) findViewById(R.id.edit_location);

        // Creamos el adaptador
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        // Obtenemos el identificador del registro si viene indicado
        if (extra.containsKey(WalletDBAdapter.C_ID)){
            id = extra.getLong(WalletDBAdapter.C_ID);
            Log.i("ID ---> ", String.valueOf(id));
            consultar(id);
        }
    }

    private void consultar(long id){
        // Consultamos el centro por el identificador
        this.cursor = dbAdapter.getRegistro(id);

        this.concepto.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CONCEPTO)));
        this.descripcion.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_DESCRIPCION)));
        this.cantidad.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CANTIDAD)));
        this.location.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_LOCALIZACION)));
    }

    private void setEdicion(boolean opcion){
        concepto.setEnabled(opcion);
        descripcion.setEnabled(opcion);
        cantidad.setEnabled(opcion);
        location.setEnabled(opcion);
    }
}