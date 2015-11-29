package com.example.cristian.mywallet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends Activity {

    private static final long ONE_MIN = 1000 * 30;
    private static final long TWO_MIN = ONE_MIN * 2;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static final long MEASURE_TIME = 1000 * 30;
    private static final long POLLING_FREQ = 1000 * 10;
    private static final float MIN_ACCURACY = 25.0f;
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final float MIN_DISTANCE = 10.0f;

    // Views for display location information
    private TextView mAccuracyView;
    private TextView mTimeView;
    private TextView mLatView;
    private TextView mLngView;

    private GoogleMap mMap;

    private int mTextViewColor = Color.GRAY;

    // Current best location estimate
    private Location mBestReading;

    // Reference to the LocationManager and LocationListener
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    private final String TAG = "LocationGetLocationActivity";

    private boolean mFirstUpdate = true;

    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_location);

        mAccuracyView = (TextView) findViewById(R.id.accuracy_view);
        mTimeView = (TextView) findViewById(R.id.time_view);
        mLatView = (TextView) findViewById(R.id.lat_view);
        mLngView = (TextView) findViewById(R.id.lng_view);

        // Acquire reference to the LocationManager
        if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
            finish();

        // Get best last location measurement
        mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, ONE_MIN);

        // Display last reading information
        if (null != mBestReading) {
            updateDisplay(mBestReading);
        } else {
            mAccuracyView.setText("No Initial Reading Available");
        }

        mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                ensureColor();

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Determine whether initial reading is
        // "good enough". If not, register for
        // further location updates

        if (null == mBestReading || mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
                                 || mBestReading.getTime() < System.currentTimeMillis() - TWO_MIN) {

            // Register for network location updates
            if (null != mLocationManager
                    .getProvider(LocationManager.NETWORK_PROVIDER)) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        double Lat  = location.getLatitude(),
               Long = location.getLongitude();
        mAccuracyView.setText("Accuracy:" + location.getAccuracy());
        mTimeView.setText("Time:"+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).format(new Date(location.getTime())));
        mLngView.setText("Longitude:" + Long);
        mLatView.setText("Latitude:" + Lat);

        Intent i = new Intent();
        i.putExtra("LAT", Lat);
        i.putExtra("LONG",Long);
        setResult(RESULT_OK, i);
        finish();
    }

    private void ensureColor() {
        if (mFirstUpdate) {
            setTextViewColor(mTextViewColor);
            mFirstUpdate = false;
        }
    }

    private void setTextViewColor(int color) {
        mAccuracyView.setTextColor(color);
        mTimeView.setTextColor(color);
        mLatView.setTextColor(color);
        mLngView.setTextColor(color);
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(Action.TYPE_VIEW,"Location Page",Uri.parse("http://host/path"),Uri.parse("android-app://com.example.cristian.mywallet/http/host/path"));
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