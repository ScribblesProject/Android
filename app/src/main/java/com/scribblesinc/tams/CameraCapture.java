package com.scribblesinc.tams;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
public class CameraCapture extends AppCompatActivity{

    private int REQUEST_Camera_RESULT =1 ;
    private static final String TAG = CameraCapture.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //run parents method by extending the existing class or run this
        //class in addition to parent's class
        super.onCreate(savedInstanceState);
        //activity class creates window
        setContentView(R.layout.activity_camera);
        //Instantiating the toolbar of adding asset activity

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == REQUEST_Camera_RESULT){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //use camera

            } else {
                Toast.makeText(getApplicationContext(), "camera Permission Required", Toast.LENGTH_SHORT).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void currentAudioAction(MenuItem item){


        if(Build.VERSION.SDK_INT >= 23){ //6.0+
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                //calculate current location

                Toast.makeText(getApplicationContext(), "Audio Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                //rejected permission request
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)){
                    Toast.makeText(getApplicationContext(), "Audio Permission Required", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Audio Permission Required");
                }
                requestPermissions(new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_Camera_RESULT);
            }
        }else{ // < 6.0
            Toast.makeText(getApplicationContext(), "Audio Permission Granted", Toast.LENGTH_SHORT).show();

        }
    }
}
