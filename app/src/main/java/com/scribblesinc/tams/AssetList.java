package com.scribblesinc.tams;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.Assets;
import com.scribblesinc.tams.network.HttpJson;
import com.scribblesinc.tams.network.HttpResponse;
import com.scribblesinc.tams.network.HttpTask;

import java.util.ArrayList;

public class AssetList extends AppCompatActivity {
    public RequestQueue queue;
    public StringRequest stringRequest;
    private static final String TAG = AssetList.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gets action bar that's supported if null
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        fetchAssets();
        testAssetCreate();
    }

    private void fetchAssets() {
        try {
            String url = "https://tams-142602.appspot.com/api/asset/list/";
            HttpJson.requestJSON(Request.Method.GET, url, null, null, new Response.Listener<HttpResponse>() {
                @Override
                public void onResponse(HttpResponse result) {

                    System.out.println("RESULT [" + result.getResponseCode() + "]");

                    if (result.getText() != null) {
                        System.out.println("TEXT: " + result.getText());
                    }

                    if (result.getJsonElement() != null) {
                        System.out.println("JSON: " + result.getJsonElement().getAsJsonObject().getAsJsonArray("assets").toString());
                    }

                }
            });
        }
        catch (Exception e) {

        }
    }

    private void testAssetCreate() {
        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();
        locations.add(new AssetLocation(0.1, 0.2));
        locations.add(new AssetLocation(0.3, 0.4));
        JsonObject json = Assets.createJSON("Charizard", "description1","categ1", "categDesc1", "typeName1", locations);

        try {
            String url = "https://tams-142602.appspot.com/api/asset/create/";
            HttpTask task = HttpJson.requestJSON(Request.Method.POST, url, json, null, new Response.Listener<HttpResponse>() {
                @Override
                public void onResponse(HttpResponse result) {
                    System.out.println("RESULT [" + result.getResponseCode() + "]");

                    if (result.getText() != null) {
                        System.out.println("TEXT: " + result.getText());
                    }

                    if (result.getJsonElement() != null) {
                        System.out.println("JSON: " + result.getJsonElement().getAsJsonObject().toString());
                    }
                }
            });

            task.setDownloadProgressListener(new Response.Listener<Double>() {
                @Override
                public void onResponse(Double progress) {
                    System.out.println("Download Progress: " + progress);
                }
            });

            task.setUploadProgressListener(new Response.Listener<Double>() {
                @Override
                public void onResponse(Double progress) {
                    System.out.println("Upload Progress: " + progress);
                }
            });

        }
        catch (Exception e) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asset_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //when the edit button is pressed
        if(id == R.id.action_edit_asset){
            Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();
        }

        //if the add asset button is pressed
        if(id == R.id.action_add_asset){
            /*Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();*/
            Intent intent = new Intent(this, AssetAdd.class);
            startActivity(intent);
        }

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }

}
