package com.example.cristian.mywallet;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditActivity extends Activity {

    private WalletDBAdapter dbAdapter;
    private Cursor cursor, cursorPres;

    private  List<String> L;
    List lista = new ArrayList<String>() {};

    // Modo del formulario
    private int modo ;

    // Identificador del registro que se edita cuando la opción es MODIFICAR
    long mId ;
    double cant_gasto, presDisponible, presTotal;

    // Elementos de la vista
    private EditText mConcepto, mDescripcion, mCantidad;
    private TextView mLocation;
    private Spinner spinner;
    private GoogleMap map;
    private LatLng myLocation;
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

        lista.add("Comida");
        lista.add("Lujos");
        lista.add("Básicos");
        lista.add("Caprichos");
        lista.add("Mensual");
        lista.add("Transporte");
        lista.add("Otros");

        DatosPorDefecto();

        if (extra == null) return;

        // Obtenemos los elementos de la vista
        mConcepto = (EditText) findViewById(R.id.edit_concepto);
        mDescripcion = (EditText) findViewById(R.id.edit_desc);
        mCantidad = (EditText) findViewById(R.id.edit_cantidad);
        mLocation = (TextView) findViewById(R.id.edit_location);

        // Creamos el adaptador
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        // Obtenemos el identificador del registro si viene indicado
        if (extra.containsKey(WalletDBAdapter.C_ID)){
            mId = extra.getLong(WalletDBAdapter.C_ID);
            consultar(mId);

            myLocation = new LatLng(Double.parseDouble(L.get(0)),Double.parseDouble(L.get(1)));

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            map.getUiSettings().setScrollGesturesEnabled(false);
            Marker location = map.addMarker(new MarkerOptions().position(myLocation).title("Wallet Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_logo)));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
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


        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
    }

    private void consultar(long id){
        // Consultamos por el identificador
        this.cursor = dbAdapter.getRegistro(id);

        mConcepto.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CONCEPTO)));
        mDescripcion.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_DESCRIPCION)));
        mCantidad.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CANTIDAD)));
        mLocation.setText(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_LOCALIZACION)));
        cant_gasto = Double.parseDouble(mCantidad.getText().toString());
        spinner.setSelection(lista.indexOf(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_CATEGORIA))));
        L = Arrays.asList(cursor.getString(cursor.getColumnIndex(WalletDBAdapter.C_LOCALIZACION)).split("\\s*,\\s*"));
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
                reg.put(WalletDBAdapter.C_LOCALIZACION, myLocation.latitude+","+myLocation.longitude);
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