package com.scribblesinc.tams;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Response;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.Assets;
import java.util.ArrayList;

/*
 * This is the main activity where the map is located at. Most of the methods are self explanatory and follow the process life cycle:
 * OnCreate() -> OnStart() -> OnResume() -> Activity Running -> On Pause() -> OnStop() -> OnDestroy().
 *
 * getLocationPermission() is only used in Android 6.0+. It checks to see if location permissions have been granted on app start up. If a user chooses
 * not to enable these permissions the app simply closes.
 *
 * isLocationSettingsEnabled() checks to see if the location settings are enabled. If they are it checks to see which one is picked (High Accuracy, Battery Saving, and GPS only) and
 * sets the location requested priority to the corresponding priority value (PRIORITY_HIGH_ACCURACY, PRIORITY_BALANCED_POWER_ACCURACY, and PRIORITY_LOW_POWER). Remember that wifi can replace
 * a users network in getting data too. If it's not active the user will be shown a prompt and then be asked to enable them. If a user still decides to disable them then a snackbar will be permanency
 * disable until a user enables location settings.
 *
 * populateMap() takes care display the map to the user. It also takes care of display the polylines too. If a filter is active then it will choose which assets to display
 *
 * constructAssetsOnMap() the drawing of assets is down here. This shouldn't need to be changed.
 *
 * onLocationChanged() takes care of updating the users location on the map
 *
 * onRequestPermissions() this is where the permission goes to see if a user has accepted or rejected. The closing the app is done here
 *
 * setMapLocationEnable() is responsible for displaying the GPS icon in Google Maps. It needs to check to see if location permissions are accepted to work.
 *
 */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 0;
    private static final int ACTIVITY_FILTER = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ASSET_FILTER = "ASSET_FILTER";
    private String category = null;
    private String type = null;
    private double mLocationLatiude, mLocationLongitude;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastKnownLocation;
    private GoogleMap mMap;
    private LatLng mLatLng;
    private boolean mRequestingLocationUpdates, cancelButtonPressed;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private View view;
    private Menu mainActivityMenu;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        setContentView(R.layout.activity_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);  // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);

        if(view == null) {//view used for snackbars
            view = findViewById(R.id.content_main);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.map_view_name);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) { //called from getMapAsync
        getLocationPermission();
        mMap = googleMap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // Inflate the menu; this adds items to the action bar if it is present.
        mainActivityMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false) {
            mGoogleApiClient.connect();
        }
        populateMap();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false) {
            mGoogleApiClient.connect();
        }
        if(mMap != null){
            populateMap();
        }
    }

    ///Running activity now
    public void getLocationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionDenied = PackageManager.PERMISSION_DENIED;
            if (permissionCheck == permissionDenied) {
                Log.d(TAG, "Location Permission Required");
                AlertDialog.Builder getLocationPermission = new AlertDialog.Builder(this);
                getLocationPermission.setTitle(R.string.location_permission_title);
                getLocationPermission.setMessage(R.string.location_permission_message);
                getLocationPermission.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
                    }
                }).create().show();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_FINE_LOCATION);
            }
        }else{
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);

        isLocationSettingsEnabled();
        setMapLocationEnable();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            mRequestingLocationUpdates = true;
        }
    }

    private void isLocationSettingsEnabled() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if((locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true|| wifiManager.isWifiEnabled() == true) && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
            mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        }else{
            if(cancelButtonPressed == false) {
                Log.d(TAG, "Location Services not enabled");
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmationAlertDialogStyle);
                builder.setTitle(R.string.location_enable_title);
                builder.setMessage(R.string.lcoation_enable_message);
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { //if ok
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //on press cancel
                        dismissSnackbar();
                        snackbar = Snackbar.make(view, R.string.location_settings_disabled, Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction(R.string.location_settings_enable,  new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent locationMenu = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(locationMenu);
                                cancelButtonPressed = false;
                                dismissSnackbar();
                            }
                        });
                        snackbar.setActionTextColor(Color.argb(255, 2, 6, 209));
                        snackbar.show();
                        cancelButtonPressed = true;
                    }
                });
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { //if ok
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //on press send the category and filter in an array to the main activity
                        dismissSnackbar();
                        Intent locationMenu = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(locationMenu);
                        cancelButtonPressed = false;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private void populateMap(){
        Assets.list(new Response.Listener<ArrayList<Assets>>() {
            @Override
            public void onResponse(ArrayList<Assets> response) {
                if(category == null && type == null){
                    for(int i = 0; i < response.size(); i++){
                        constructAssetsOnMap(response, i);
                    }
                }else{
                    for(int i = 0; i < response.size(); i++){
                        if(category.equalsIgnoreCase(response.get(i).getCategory()) && type.equalsIgnoreCase(response.get(i).getAsset_type())) {
                            constructAssetsOnMap(response, i);
                        }
                    }
                }
            }
        }, null);
    }

    private void constructAssetsOnMap(ArrayList<Assets> response, int i){
        ArrayList<AssetLocation> sortedLocations = response.get(i).getSortedLocations();
        PolylineOptions newLine;
        LatLng newLatLng;
        if(sortedLocations.size() > 1){
            newLine = new PolylineOptions();
            for(int j = 0; j < sortedLocations.size(); j++){
                newLatLng = new LatLng(sortedLocations.get(j).getLatitude(), sortedLocations.get(j).getLongitude());
                mMap.addMarker(new MarkerOptions().position(newLatLng).title(response.get(i).getName()).snippet(response.get(i).getDescription())).setVisible(true);
                newLine.add(newLatLng).color(Color.RED).width((float)2.5);
            }
            newLatLng = new LatLng(sortedLocations.get(0).getLatitude(), sortedLocations.get(0).getLongitude());
            mMap.addMarker(new MarkerOptions().position(newLatLng).title(response.get(i).getName()).snippet(response.get(i).getDescription())).setVisible(true);
            newLine.add(newLatLng).color(Color.RED).width((float)2.5);
            mMap.addPolyline(newLine);
        }else if(sortedLocations.size() == 1){
            newLatLng = new LatLng(sortedLocations.get(0).getLatitude(), sortedLocations.get(0).getLongitude());
            mMap.addMarker(new MarkerOptions().position(newLatLng).title(response.get(i).getName()).snippet(response.get(i).getDescription())).setVisible(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mapFragment.getView().getVisibility() == View.INVISIBLE){
            mapFragment.getView().setVisibility(View.VISIBLE);
        }

        mLocationLatiude = location.getLatitude();
        mLocationLongitude = location.getLongitude();
        mLatLng = new LatLng(mLocationLatiude, mLocationLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        if(mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mRequestingLocationUpdates = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0)) {
                    Toast.makeText(getApplicationContext(), "Closing TAMS", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    setMapLocationEnable();
                }
            default:
                System.out.println("\nRequest Code: " + requestCode + "not handled\n");
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setMapLocationEnable() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionDenied = PackageManager.PERMISSION_DENIED;
        if(permissionCheck == permissionDenied) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter && item.getTitle().equals(this.getText(R.string.action_filter))) {
            if(cancelButtonPressed== false) {
                Intent intent = new Intent(MainActivity.this, AssetFilter.class);
                startActivityForResult(intent, ACTIVITY_FILTER);
            }
        }else if(id == R.id.action_filter && item.getTitle().equals(this.getText(R.string.clear_filter))) {
            if(cancelButtonPressed == false) {
                category = null;
                type = null;
                item.setTitle(R.string.action_filter);
                snackbar.dismiss();
                mMap.setPadding(0, 0, 0, 0);
                populateMap();
            }
        }

        //when the list button is pressed
        if (id == R.id.action_list) {
            Intent intent = new Intent(this, AssetList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//this is where you handle intent request
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) {
            return;
        }
        switch(requestCode) {
            case ACTIVITY_FILTER: //the user selected category and type gets initialized here
                String[] categoryTypeFilter = data.getStringArrayExtra(ASSET_FILTER); //get the data passed from the intent done in asset filter
                category = categoryTypeFilter[0]; // sets the category
                type = categoryTypeFilter[1]; //sets the type

                MenuItem clearFilter = mainActivityMenu.findItem(R.id.action_filter); //gets teh menu item "filter"
                clearFilter.setTitle(R.string.clear_filter); //changes the name to clear filter

                mMap.clear(); //clears the map
                mMap.setPadding(0,0,0,50);
                populateMap(); //and re populates it

                dismissSnackbar();
                snackbar = Snackbar.make(view, R.string.filter_applied , Snackbar.LENGTH_INDEFINITE); //sets the snack bar (it appears at the bottom)
                snackbar.show(); //and shows it
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        dismissSnackbar();
        if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            snackbar = Snackbar.make(view, R.string.connection_lost_network, Snackbar.LENGTH_INDEFINITE);
        }else if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            snackbar = Snackbar.make(view, R.string.connection_lost_service, Snackbar.LENGTH_INDEFINITE);
        }
        snackbar.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        dismissSnackbar();
        snackbar = Snackbar.make(view, R.string.connection_failed, Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    private void dismissSnackbar(){
        if(snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if(mMap != null) {
            mMap.clear();
        }
        if(mRequestingLocationUpdates == true) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mRequestingLocationUpdates = false;
        }
    }

    @Override
    protected void onStop() {
        if(mRequestingLocationUpdates == true) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mRequestingLocationUpdates = false;
        }
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}