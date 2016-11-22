package com.scribblesinc.tams;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.wearable.Asset;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.Assets;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    private double mLocationLatiude, mLocationLongitude;
    private String mLocationLatiudeText, mLocationLongitudeText;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastKnownLocation;
    private GoogleMap mMap;
    private LatLng mLatLng;
    private Marker mMarker;
    private boolean mRequestingLocationUpdates;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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

        //when the filter button is pressed
        if (id == R.id.action_filter) {
            Toast.makeText(getApplicationContext(), "Not Working Yet", Toast.LENGTH_SHORT).show();
        }

        //when the list button is pressed
        if (id == R.id.action_list) {
            Intent intent = new Intent(this, AssetList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //this method is called when the current location menu item is tapped
    //public void currentLocationAction(MenuItem item) {
    //Will work on later. Need to figure out how to get the device location here.
    //}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocationPermission();
        mMap = googleMap;

        populateMap(googleMap);
        // Hard-code marker by CSUS to test maps
        //LatLng csus = new LatLng(38.559144, -121.4256621);
        //mMap.addMarker(new MarkerOptions().position(csus).title("CSUS")).setVisible(true);
        // Move the camera instantly to location with a zoom of 15.
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(csus, 15));
        // Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    private void populateMap(final GoogleMap googleMap){

        Assets.list(new Response.Listener<ArrayList<Assets>>() {
            @Override
            public void onResponse(ArrayList<Assets> response) {
                ArrayList<AssetLocation> sortedLocations;
                PolylineOptions newLine = new PolylineOptions();
                LatLng newLatLng;

                for (int i = 0; i < response.size(); i++){
                    //response.get(i).getLocations().get("" + j).getLatitude();`
                    //LatLng assetPoint = new LatLng(response.get(i).getSortedLocations().get(i).getLatitude(), response.get(i).getSortedLocations().get(j).getLongitude());
                    sortedLocations = response.get(i).getSortedLocations();
                    if(sortedLocations.size() > 1) {
                        for (int j = 0; j < sortedLocations.size(); j++) {
                            newLatLng = new LatLng(sortedLocations.get(j).getLatitude(), sortedLocations.get(j).getLongitude());
                            //sets the lines location, color, and width
                            newLine.add(newLatLng).color(Color.RED).width((float)2.5);
                            googleMap.addMarker(new MarkerOptions().position(newLatLng).title(response.get(i).getName()).snippet(response.get(i).getDescription())).setVisible(true);
                        }

                        newLatLng = new LatLng(sortedLocations.get(0).getLatitude(), sortedLocations.get(0).getLongitude());
                        //sets the lines location, color, and width
                        newLine.add(newLatLng).color(Color.RED).width((float)2.5);
                        mMap.addPolyline(newLine);

                    } else if(sortedLocations.size() == 1){
                        newLatLng = new LatLng(sortedLocations.get(0).getLatitude(), sortedLocations.get(0).getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(newLatLng).title(response.get(i).getName()).snippet(response.get(i).getDescription())).setVisible(true);
                    }
                }
            }
        }, null);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        if(mLastKnownLocation != null) {
            mLocationLatiudeText = String.valueOf(mLastKnownLocation.getLatitude());
            mLocationLongitudeText = String.valueOf(mLastKnownLocation.getLongitude());
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        startLocationUpdates();

        //LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);

        //Toast.makeText(MainActivity.this, "Map Connection Established", Toast.LENGTH_SHORT).show();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        mRequestingLocationUpdates = true;
    }

    /*private void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        mRequestingLocationUpdates = false;
    }*/

    @Override
    public void onConnectionSuspended(int i) {
        if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
            Toast.makeText(getApplicationContext(), "Connection Lost due to Network", Toast.LENGTH_LONG).show();
        }else if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getApplicationContext(), "Connection Lost due to Service", Toast.LENGTH_LONG).show();
        }

        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Failed To Connect", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocationLatiudeText = String.valueOf(location.getLatitude());
        mLocationLongitudeText = String.valueOf(location.getLongitude());
        mLocationLatiude = location.getLatitude();
        mLocationLongitude = location.getLongitude();
        mLatLng = new LatLng(mLocationLatiude, mLocationLongitude);

        if(mMarker != null) {
            mMarker.remove();
        }

        //mMarker = mMap.addMarker(new MarkerOptions().position(mLatLng).title("Your Position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        if(mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public void getLocationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionDenied = PackageManager.PERMISSION_DENIED;
            if (permissionCheck == permissionDenied) {
                Log.d(TAG, "AssetLocation Permission Required");
                AlertDialog.Builder getLocationPermission = new AlertDialog.Builder(this);
                getLocationPermission.setTitle("AssetLocation Required");
                getLocationPermission.setMessage("TAMS needs to use your device's location in order to function properly. If you accept the please tap 'ok' and then tap 'allow'.");
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
            Toast.makeText(getApplicationContext(), "AssetLocation Permission Granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0) {
                    Toast.makeText(getApplicationContext(), "AssetLocation Permission Granted", Toast.LENGTH_LONG).show();
                    setLocationEnable();
                } else {
                    Toast.makeText(getApplicationContext(), "Closing TAMS", Toast.LENGTH_LONG).show();
                    finish();
                }
            default:
                System.out.println("\nRequest Code: " + requestCode + "not handled\n");
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setLocationEnable() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionDenied = PackageManager.PERMISSION_DENIED;
        if(permissionCheck == permissionDenied) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}