package com.scribblesinc.tams;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.scribblesinc.tams.androidcustom.AssetItems;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AssetList extends AppCompatActivity {
    public RequestQueue queue;
    public StringRequest stringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        queue = Volley.newRequestQueue(this);
        String url = "https://tams-142602.appspot.com/";

        stringRequest = new StringRequest(Request.Method.GET, url+"api/asset/list/", new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                      //  Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();
                        //use Gson to do a display of data
                        Gson gson  = new Gson();

                    }
                }, new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Toast.makeText(getApplicationContext(), "Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        //add the request to the RequestQueue
        queue.add(stringRequest);
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
