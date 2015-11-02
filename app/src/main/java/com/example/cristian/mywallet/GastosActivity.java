package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;

import com.example.cristian.mywallet.GastosList;
import java.util.ArrayList;

public class GastosActivity extends AppCompatActivity {
    GastosList gastos;
    static private final int GET_TEXT_REQUEST_CODE = 1;
    ListView listV;
    ShareActionProvider mShareActionProvider;
    private static final int MENU_ADD = Menu.FIRST+1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        //Get GastosList
        gastos = new GastosList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Register listView
       

        // Register onClickListener to handle click events on each item
        /*listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ItemClicked item = adapter.getItem(position);
                //Intent intent = new Intent(Activity.this,destinationActivity.class);
                //based on item add info to intent
                //startActivity(intent);
            }
        });
        */
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        listV=(ListView) findViewById(R.id.listView);
        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,gastos.getListaGastos());
        // Set adapter to listView
        listV.setAdapter(arrayAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // TODO: Save state information with a collection of key-value pairs
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // RESULT_OK result code and a recognized request code
        // If so, update the Textview showing the user-entered text.
        if(requestCode == GET_TEXT_REQUEST_CODE){
            if(resultCode == RESULT_OK){ //COMPROBAR SI ESTA BIEN LO QUE HA LLEGADO
                Gastos g = (Gastos) data.getSerializableExtra("GASTO");
                Log.d("CONTENIDO RECIBIDO","Cantidad: " + g.getCantidad() + "\n" +
                                           "Concepto: " + g.getConcepto() + "\n" +
                                           "Descripcion: " + g.getDescripcion() + "\n\n");
                this.gastos.addElementoLista(g);
                refreshGastos();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addGasto:
                Intent i=new Intent(getBaseContext(),AddGastosActivity.class);
                startActivityForResult(i,GET_TEXT_REQUEST_CODE);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshGastos(){
        listV=(ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.list_item,gastos.getListaGastos());
        // Set adapter to listView
        listV.setAdapter(arrayAdapter);
    }

}
