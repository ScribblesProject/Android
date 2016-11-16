package com.scribblesinc.tams;


import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.List;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scribblesinc.tams.androidcustom.AssetItems;
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.androidcustom.Item;
import com.scribblesinc.tams.patterns.AppController;
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.androidcustom.AssetItems;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetList extends AppCompatActivity {
    public RequestQueue queue;
    public StringRequest stringRequest;
    private static final String TAG = AssetList.class.getSimpleName();
    private CustomListAdapter listadapter;
    private ListView listview;
    private Gson gson;
    //private List<Assets>assetlist = new ArrayList<Assets>();
    private String name = "";
    private String description = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //gets action bar that's supported if null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        queue = Volley.newRequestQueue(this);
        String url = "https://tams-142602.appspot.com/";


        stringRequest = new StringRequest(Request.Method.GET, url + "api/asset/list/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_SHORT).show();

                //use Gson to do a display of data
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, ArrayList<Assets>>>() {
                }.getType();

                Map<String, ArrayList<Assets>> result = gson.fromJson(response, type);
                /*
                ArrayList<Assets> assets = result.get("assets");
                Log.d(TAG, String.valueOf(assets.get(0).sortedLocations()));
                Log.d(TAG, String.valueOf(result.get("assets")));
                //Log.d(TAG, String.valueOf(result.get("assets").get(0).getName()));
                Toast.makeText(getApplicationContext(), result.get("assets").toString(), Toast.LENGTH_LONG).show();
                 */
                //Initialize adapter
                listview = (ListView) findViewById(R.id.listView_al);
                listadapter = new CustomListAdapter(getApplicationContext(),result);
                listview.setAdapter(listadapter);

                //notify list adapter that data has new changes
                listadapter.notifyDataSetChanged();

             //   List<Assets> assets = (List<Assets>) gson.fromJson(response, type);
                //register listview
  ///              listview = (ListView) findViewById(R.id.listView_al);
                //listadapter = new CustomListAdapter(this,assets);

                //got to figure out how to make it only read image, titile and discription and pass this to the adapter.


                //2 notify list adapter about data change/obtains list id to be use with AssetList
               //listview = getListView();
//                listview.setAdapter(listadapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //add the request to the RequestQueue
        //queue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }//endof Oncreate


    // The array list of items to be added to my ListView
    private ArrayList<Item>generateData() {
        ArrayList<Item> items = new ArrayList<>();
        //items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name", name));
        items.add(new Item("Description", description));

        return items;
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
