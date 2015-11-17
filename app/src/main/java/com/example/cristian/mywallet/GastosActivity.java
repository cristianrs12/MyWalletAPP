package com.example.cristian.mywallet;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GastosActivity extends AppCompatActivity {

    static private final int GET_TEXT_REQUEST_CODE = 1;

    private WalletCursorAdapter walletAdapter ;
    private PresupuestoCursorAdapter presAdapter ;
    private WalletDBAdapter dbAdapter;
    private Cursor cursor, cursor2;

    public static double V_PRESUPUESTO = 0;
    public static final String SIN_PRESUPUESTO  = "Sin presupuesto" ;
    public static final String C_MODO  = "modo" ;
    public static final int C_VISUALIZAR = 551 ;
    public static final int C_CREAR = 552 ;

    GastosList gastos;
    ListView listV;
    //ListView presupuesto;
    TextView presu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);
        listV=(ListView) findViewById(R.id.listView);
        //presupuesto=(ListView) findViewById(R.id.presupuesto);
        presu= (TextView) findViewById(R.id.presu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        getDataDB();
        getPresup();

        Toast.makeText(getBaseContext(), "Base de datos lista", Toast.LENGTH_LONG).show();

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(GastosActivity.this, EditActivity.class);
                i.putExtra(C_MODO, C_VISUALIZAR);
                i.putExtra(WalletDBAdapter.C_ID, id);
                startActivityForResult(i, C_VISUALIZAR);
            }
        });
    }

    private void getPresup(){
        double presupuesto=0;
        double disponible=0;
        Cursor prep;
        prep=dbAdapter.getCursorPrep();
        Constants.presupuesto=prep.getDouble(prep.getColumnIndex(WalletDBAdapter.C_PRESUPUESTO));
        Constants.disponible=prep.getDouble(prep.getColumnIndex(WalletDBAdapter.C_DISPONIBLE));
        presupuesto=Constants.disponible;
        if(presupuesto==0){
            Log.e("SIN RESULTADOS", "EL CURSOR NO HA OBTENIDO RESULTADOS");
            Intent i = new Intent(GastosActivity.this, PrepActivity.class);
            startActivityForResult(i, RESULT_OK);
        }
        else {
            disponible = Constants.disponible;
            presu.setText("Presupuesto: "+Double.toString(presupuesto)+"€ - Disponible: " + Double.toString(disponible) + "€" );
        }
        /* presupuesto=Constants.presupuesto;
        if(presupuesto==0){
            Log.e("SIN RESULTADOS", "EL CURSOR NO HA OBTENIDO RESULTADOS");
            Intent i = new Intent(GastosActivity.this, PrepActivity.class);
            startActivityForResult(i, RESULT_OK);
        }
        else {
            disponible = Constants.disponible;
            presu.setText("Presupuesto: "+Double.toString(presupuesto)+"€ - Disponible: " + Double.toString(disponible) + "€" );
        }*/

    }

    private void updatePres(){
        double presupuesto=0;
        double disponible=0;
        presupuesto=Constants.presupuesto;
        disponible = Constants.disponible;
        presu.setText("Presupuesto: "+Double.toString(presupuesto)+"€ - Disponible: " + Double.toString(disponible) + "€" );
    }

    private void getDataDB() {
        cursor = dbAdapter.getCursor();
        walletAdapter = new WalletCursorAdapter(this, cursor);
        listV.setAdapter(walletAdapter);
    }

    @Override
    public void onStart() {super.onStart();}

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
        getDataDB();
        updatePres();
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
    public void onRestart() {super.onRestart();}

    @Override
    public void onDestroy() {super.onDestroy();}

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // RESULT_OK result code and a recognized request code
        if(requestCode == GET_TEXT_REQUEST_CODE){
            if(resultCode == RESULT_OK){
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
}