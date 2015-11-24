package com.example.cristian.mywallet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends Activity {

    private WalletDBAdapter dbAdapter;
    private Cursor cursor, cursorPres;

    // Modo del formulario
    private int modo ;

    // Identificador del registro que se edita cuando la opción es MODIFICAR
    long mId ;
    double cant_gasto, presDisponible, presTotal;

    // Elementos de la vista
    EditText mConcepto, mDescripcion, mCantidad, mLocation;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Button save, del, cancel;
        save = (Button) findViewById(R.id.boton_guardar);
        del = (Button) findViewById(R.id.boton_borrar);
        cancel = (Button) findViewById(R.id.boton_cancelar);
        spinner = (Spinner) findViewById(R.id.spinner);
        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        DatosPorDefecto();
        if (extra == null) return;

        // Obtenemos los elementos de la vista
        mConcepto = (EditText) findViewById(R.id.edit_concepto);
        mDescripcion = (EditText) findViewById(R.id.edit_desc);
        mCantidad = (EditText) findViewById(R.id.edit_cantidad);
        mLocation = (EditText) findViewById(R.id.edit_location);

        // Creamos el adaptador
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        // Obtenemos el identificador del registro si viene indicado
        if (extra.containsKey(WalletDBAdapter.C_ID)){
            mId = extra.getLong(WalletDBAdapter.C_ID);
            consultar(mId);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarItem();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void consultar(long id){
        // Consultamos por el identificador
        this.cursor = dbAdapter.getRegistro(id);
        //this.cursorPres = dbAdapter.getRegistroPres(0);

        //presDisponible = cursorPres.getDouble(cursorPres.getColumnIndex(WalletDBAdapter.C_DISPONIBLE));
        //presTotal = cursorPres.getDouble(cursorPres.getColumnIndex(WalletDBAdapter.C_PRESUPUESTO));

        mConcepto.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CONCEPTO)));
        mDescripcion.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_DESCRIPCION)));
        mCantidad.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CANTIDAD)));
        cant_gasto = Double.parseDouble(mCantidad.getText().toString());
        mLocation.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_LOCALIZACION)));
    }

    private void eliminarItem() {
        AlertDialog.Builder dialogEliminar = new AlertDialog.Builder(this);
        dialogEliminar.setIcon(android.R.drawable.ic_dialog_alert);
        dialogEliminar.setTitle(getResources().getString(R.string.ok_eliminar));
        dialogEliminar.setMessage(getResources().getString(R.string.text_eliminar));
        dialogEliminar.setCancelable(false);
        dialogEliminar.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int boton) {
                long id = mId;
                Constants.disponible=Constants.disponible+cant_gasto;
                dbAdapter.delete(id);

                ContentValues regPres = new ContentValues();
                regPres.put(WalletDBAdapter.C_ID, 1);
                regPres.put(WalletDBAdapter.C_PRESUPUESTO, Constants.presupuesto);
                regPres.put(WalletDBAdapter.C_DISPONIBLE, Constants.disponible);
                dbAdapter.updatePrep(regPres);
                Toast.makeText(EditActivity.this, "Gasto eliminado correctamente", Toast.LENGTH_SHORT).show();
                // Devolvemos el control
                setResult(RESULT_OK);
                finish();
            }
        });
        dialogEliminar.setNegativeButton(android.R.string.no, null);
        dialogEliminar.show();
    }

    private void guardarCambios() {

        final String concepto, descripcion, categoria;
        double cant, newPres;
        long id;

        id = mId;
        concepto = mConcepto.getText().toString();
        descripcion = mDescripcion.getText().toString();
        //Comprueba que el campo "Concepto" no esté vacio
        if(concepto.isEmpty()){
            mConcepto.setError("Concepto obligatorio");
        //Comprueba que el campo "Descripcion" no esté vacio
        } else if (descripcion.isEmpty()) {
            mDescripcion.setError("Descripción obligatoria");
        //Comprueba que el campo "Cantidad" no esté vacio
        } else {
            if (TextUtils.isEmpty(mCantidad.getText().toString())) {
                mCantidad.setError("Cantidad obligatoria");
            } else {
                cant = Double.parseDouble(mCantidad.getText().toString());
                Constants.disponible=Constants.disponible+cant_gasto;
                categoria = spinner.getSelectedItem().toString();
                Constants.disponible=Constants.disponible-cant;
                // Añadimos los datos del formulario
                ContentValues reg = new ContentValues();
                reg.put(WalletDBAdapter.C_ID, id);
                reg.put(WalletDBAdapter.C_CONCEPTO, concepto);
                reg.put(WalletDBAdapter.C_DESCRIPCION, descripcion);
                reg.put(WalletDBAdapter.C_CANTIDAD, cant);
                reg.put(WalletDBAdapter.C_LOCALIZACION, "");
                reg.put(WalletDBAdapter.C_CATEGORIA, categoria);

                dbAdapter.update(reg);

                Toast.makeText(EditActivity.this, "Gasto modificado correctamente", Toast.LENGTH_SHORT).show();

                ContentValues regPres = new ContentValues();
                regPres.put(WalletDBAdapter.C_ID, 1);
                regPres.put(WalletDBAdapter.C_PRESUPUESTO, Constants.presupuesto);
                regPres.put(WalletDBAdapter.C_DISPONIBLE, Constants.disponible);
                dbAdapter.updatePrep(regPres);

                Toast.makeText(EditActivity.this, "Presupuesto modificado", Toast.LENGTH_SHORT).show();
                //Actualizamos el presupuesto disponible
                /*if(cant != cant_gasto){

                    newPres = 0;
                    //El gasto tiene una cantidad menor
                    if (cant < cant_gasto) newPres = (presDisponible + cant_gasto) - cant;
                        //El gasto tiene una cantidad mayor
                    else if (cant > cant_gasto) newPres = presDisponible - cant;


                }*/

                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    private void setEdicion(boolean opcion){
        mConcepto.setEnabled(opcion);
        mDescripcion.setEnabled(opcion);
        mCantidad.setEnabled(opcion);
        mLocation.setEnabled(opcion);
    }
}