package com.example.cristian.mywallet;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddGastosActivity extends AppCompatActivity {

    EditText mConcepto;
    EditText mDesc;
    EditText mCant;

    private static final long ONE_MIN = 1000 * 30;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float MIN_DISTANCE = 10.0f;

    private LatLng latlng = new LatLng(0,0);

    // Views for display location information
    private TextView mActualLocation;

    //Google Map definition
    private GoogleMap mMap;

    // Current best location estimate
    private Location mBestReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private final String TAG = "GetLocation";

    private boolean mFirstUpdate = true;

    private GoogleApiClient client;
    private GoogleMap map;

    double presDisponible, presTotal;
    private Cursor cursorPres;

    private WalletDBAdapter dbAdapter;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gastos);

        mConcepto = (EditText) findViewById(R.id.concepto);
        mCant = (EditText) findViewById(R.id.cantidad);
        mDesc = (EditText) findViewById(R.id.desc);
        spinner = (Spinner) findViewById(R.id.spinner);
        mActualLocation = (TextView) findViewById(R.id.edit_location);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.getUiSettings().setScrollGesturesEnabled(false);

        Button enterButton = (Button) findViewById(R.id.add);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatosPorDefecto();

        // Creamos el adaptador
        dbAdapter = new WalletDBAdapter(this);
        dbAdapter.abrir();

        // Acquire reference to the LocationManager
        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
            finish();

        // Get best last location measurement
        mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, ONE_MIN);

        // Display last reading information
        if (null != mBestReading) {
            updateDisplay(mBestReading);
        } else {
            mActualLocation.setText("No Initial Reading Available");
            // Provider not enabled, prompt user to enable it
            Toast.makeText(this, "Por favor activa el GPS", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);
        }

        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                if (null == mBestReading
                        || location.getAccuracy() < mBestReading.getAccuracy()) {

                    // Update best estimate
                    mBestReading = location;

                    // Update display
                    updateDisplay(location);

                    if (mBestReading.getAccuracy() < MIN_ACCURACY)
                        //noinspection ResourceType
                        mLocationManager.removeUpdates(mLocationListener);
                }
            }

            public void onStatusChanged(String provider, int status,Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // Definimos la accion para el boton
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterClicked();
            }
        });
    }

    private void DatosPorDefecto() {
        List<String> lista = new ArrayList<>();
        lista.add("Comida");
        lista.add("Lujos");
        lista.add("Básicos");
        lista.add("Caprichos");
        lista.add("Mensual");
        lista.add("Transporte");
        lista.add("Otros");
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adaptador);
    }

    private void enterClicked() {
        String categoria, concepto, descripcion;
        double presupuestodisp;
        double cant, newPres;

        concepto = mConcepto.getText().toString();
        descripcion = mDesc.getText().toString();

        //Comprueba que el campo "Concepto" no esté vacio
        if(concepto.isEmpty()){
            mConcepto.setError("Concepto obligatorio");
        //Comprueba que el campo "Descripcion" no esté vacio
        } else if (descripcion.isEmpty()) {
            mDesc.setError("Descripción obligatoria");
        //Comprueba que el campo "Cantidad" no esté vacio
        } else if(TextUtils.isEmpty(mCant.getText().toString())) {
            mCant.setError("Cantidad obligatoria");
        } else {
            cant = Double.parseDouble(mCant.getText().toString());
            Constants.disponible=Constants.disponible-cant;
            categoria = spinner.getSelectedItem().toString();

            // Añadimos los datos del formulario
            ContentValues reg = new ContentValues();
            reg.put(WalletDBAdapter.C_CONCEPTO, concepto);
            reg.put(WalletDBAdapter.C_DESCRIPCION, descripcion);
            reg.put(WalletDBAdapter.C_CANTIDAD, cant);

            if(latlng.latitude!=0&&latlng.longitude!=0){
                reg.put(WalletDBAdapter.C_LOCALIZACION, latlng.latitude+","+latlng.longitude);

                reg.put(WalletDBAdapter.C_CATEGORIA,categoria);

                dbAdapter.insert(reg);
                Toast.makeText(AddGastosActivity.this, "Gasto añadido correctamente", Toast.LENGTH_SHORT).show();

                //Actualizamos el presupuesto disponible
                ContentValues regPres = new ContentValues();
                regPres.put(WalletDBAdapter.C_ID, 1);
                regPres.put(WalletDBAdapter.C_PRESUPUESTO, Constants.presupuesto);
                regPres.put(WalletDBAdapter.C_DISPONIBLE, Constants.disponible);

                dbAdapter.updatePrep(regPres);
                Toast.makeText(AddGastosActivity.this, "Presupuesto modificado", Toast.LENGTH_SHORT).show();

                Intent i= new Intent();
                setResult(RESULT_OK, i);
                finish();
            }else{
                Toast.makeText(AddGastosActivity.this, "GPS necesario para nuevos gastos", Toast.LENGTH_SHORT).show();
                Intent i= new Intent();
                setResult(RESULT_CANCELED, i);
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null == mBestReading || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                || mBestReading.getTime() < System.currentTimeMillis() - TWO_MIN) {

            // Register for network location updates
            if (null != mLocationManager
                    .getProvider(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, POLLING_FREQ,
                        MIN_DISTANCE, mLocationListener);
            }

            // Register for GPS location updates
            if (null != mLocationManager
                    .getProvider(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, POLLING_FREQ,
                        MIN_DISTANCE, mLocationListener);
            }

            // Schedule a runnable to unregister location listeners
            Executors.newScheduledThreadPool(1).schedule(new Runnable() {
                @Override
                public void run() {
                    //noinspection ResourceType
                    mLocationManager.removeUpdates(mLocationListener);
                }
            }, MEASURE_TIME, TimeUnit.MILLISECONDS);
        }
    }

    // Unregister location listeners
    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.removeUpdates(mLocationListener);
    }

    private Location bestLastKnownLocation(float minAccuracy, long maxAge) {

        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestAge = Long.MIN_VALUE;

        List<String> matchingProviders = mLocationManager.getAllProviders();

        for (String provider : matchingProviders) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }

            Location location = mLocationManager.getLastKnownLocation(provider);

            if (location != null) {
                float accuracy = location.getAccuracy();
                long time = location.getTime();

                if (accuracy < bestAccuracy) {
                    bestResult = location;
                    bestAccuracy = accuracy;
                    bestAge = time;
                }
            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy || (System.currentTimeMillis() - bestAge) > maxAge) {
            return null;
        } else {
            return bestResult;
        }
    }

    // Update display
    private void updateDisplay(Location location) {
        mActualLocation.setText("LT:" + location.getLatitude() + " LG:" + location.getLongitude());
        latlng = new LatLng(location.getLatitude(), location.getLongitude());

        Marker L = map.addMarker(new MarkerOptions().position(latlng).title("Wallet Here!").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_logo)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(Action.TYPE_VIEW,"Location Page", Uri.parse("http://host/path"),Uri.parse("android-app://com.example.cristian.mywallet/http/host/path"));
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(Action.TYPE_VIEW,"Location Page",Uri.parse("http://host/path"),Uri.parse("android-app://com.example.cristian.mywallet/http/host/path"));
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}