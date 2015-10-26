package com.example.cristian.mywallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.cristian.mywallet.GastosList;
import java.util.ArrayList;

public class GastosActivity extends AppCompatActivity {
    GastosList gastos;
    ListView listV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

        //Get GastosList
        gastos = new GastosList();

        // Register listView
        listV=(ListView) findViewById(R.id.listView);

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,gastos.getListaGastos());

        // Set adapter to listView
        listV.setAdapter(arrayAdapter);

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

        // TODO: Emit LogCat message


        // TODO:
        // Update the appropriate count variable
        // Update the user interface


    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO: Emit LogCat message


        // TODO:
        listV=(ListView) findViewById(R.id.listView);
    }

    @Override
    public void onPause() {
        super.onPause();

        // TODO: Emit LogCat message

    }

    @Override
    public void onStop() {
        super.onStop();

        // TODO: Emit LogCat message

    }

    @Override
    public void onRestart() {
        super.onRestart();

        // TODO: Emit LogCat message


        // TODO:
        // Update the appropriate count variable
        // Update the user interface


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // TODO: Emit LogCat message


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // TODO:
        // Save state information with a collection of key-value pairs
        // 4 lines of code, one for every count variable


    }

}
