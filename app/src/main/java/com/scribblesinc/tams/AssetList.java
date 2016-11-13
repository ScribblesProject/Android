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

        fetchAssets();
//        testAssetsEndpoints();
    }

    private void fetchAssets() {
        Assets.list(new Response.Listener<ArrayList<Assets>>() {
            @Override
            public void onResponse(ArrayList<Assets> response) {
                if (response != null) {
                    // ... Display this...
                    System.out.println("ASSET LIST RESPONSE: " + response);
                }
            }
        }, null);
    }

    private void testAssetsEndpoints() {

        System.out.println("CREATING....");
        //Create TEST
        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();
        locations.add(new AssetLocation(0.1, 0.2));
        locations.add(new AssetLocation(0.3, 0.4));
        Assets.create("TEST NAME", "Some Description", "Pokemon", "Fire, grass, water, etc", "Fire", locations, new Response.Listener<Long>() {
            @Override
            public void onResponse(Long response) {
                System.out.println("THE NEW ID IS: " + response);

                if (response > 0) {

                    //Fetch TEST
                    System.out.println("FETCHING....");
                    Assets.fetch(response, new Response.Listener<Assets>() {
                        @Override
                        public void onResponse(final Assets asset) {

                            //Update TEST
                            System.out.println("UPDATING....");
                            asset.setName("TEST 2");
                            asset.update(new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {
                                    if (response) {
                                        System.out.println("UPDATE SUCCESS!!");

                                        //Fetch TEST
                                        System.out.println("FETCHING....");
                                        Assets.fetch(asset.getId(), new Response.Listener<Assets>() {
                                            @Override
                                            public void onResponse(Assets response) {
                                                System.out.println("THE UPDATED ASSET: " + response);

                                                //Delete TEST
                                                System.out.println("DELETING....");
                                                response.delete(new Response.Listener<Boolean>() {
                                                    @Override
                                                    public void onResponse(Boolean response) {
                                                        if (response) {
                                                            System.out.println("DELETION SUCCESS!!");
                                                        }
                                                        else {
                                                            System.out.println("DELETION FAILED!!");
                                                        }
                                                    }
                                                }, null);

                                            }
                                        },null);
                                    }
                                }
                            }, null);

                        }
                    }, null);
                }
            }
        }, null);
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
