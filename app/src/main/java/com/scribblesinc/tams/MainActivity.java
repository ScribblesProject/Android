package com.scribblesinc.tams;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Toolbar toolbar;

    private double latitude, longitude;

    private MyLocationListener locationListener;

    /* Request constants used for permission reasons. */
    private static final int REQUEST_LOCATION_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //when the list button is pressed
        if(id == R.id.action_list){
            Intent intent = new Intent(this, AssetList.class);
            startActivity(intent);
        }

        //when the filter button is pressed
        if(id == R.id.action_filter){
            Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hard-code marker by CSUS to test maps
        //LatLng csus = new LatLng(38.559144, -121.4256621);
        //mMap.addMarker(new MarkerOptions().position(csus).title("CSUS")).setVisible(true);

        // Move the camera instantly to location with a zoom of 15.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(csus, 15));

        // Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    //this method is called when the current location menu item is tapped
    public void currentLocationAction(MenuItem item){
        //Toast.makeText(getApplicationContext(), "Location Not Yet Working", Toast.LENGTH_SHORT).show();

        if(Build.VERSION.SDK_INT >= 23){ //6.0+
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //calculate current location
                getCurrentLocation();
                //Toast.makeText(getApplicationContext(), "Locating Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                //rejected permission request
                if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                    //Toast.makeText(getApplicationContext(), "Location Permission Required", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Location Permission Required");
                }
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_RESULT);
            }
        }else{ // < 6.0
            //Toast.makeText(getApplicationContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
        }
    }

    public void getCurrentLocation(){


        /*locationListener = new MyLocationListener(MainActivity.this);

        mMap.clear();

        if(locationListener.canGetLocation){
            this.latitude = locationListener.getLatitude();
            this.longitude = locationListener.getLongitude();
            LatLng currentLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location")).setVisible(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


            Toast.makeText(MainActivity.this, "Location: \nLat: " + latitude + "\nLong: "
                    + longitude, Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_LOCATION_RESULT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //calculate current location
                getCurrentLocation();
            } else {
                Toast.makeText(getApplicationContext(), "Location Permission Required", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(MainActivity.this, "Map Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
