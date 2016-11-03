package com.scribblesinc.tams;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.androidcustom.AssetItems;
import com.scribblesinc.tams.patterns.AppController;

import java.util.ArrayList;
import java.util.List;


public class AssetList extends AppCompatActivity {
    public RequestQueue queue;
    private static final String url = "https://tams-142602.appspot.com/";
    //public StringRequest stringRequest;
    public JsonRequest jsonrequest;
    private CustomListAdapter listadapter;
    private ListView listview;
    private Gson gson;
    private List<Asset>assetlist = new ArrayList<Asset>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Instantiate a RequestQueue to handle running of request
        queue = Volley.newRequestQueue(this);//(getApplicationContext())


        fetchPost();
    }

    private void fetchPost(){
       // StringRequest request = new StringRequest(Request.Method.GET,url+"api/asset/list/",onPostsLoaded,onPostsError);
        StringRequest request = new StringRequest(Request.Method.GET,url+"api/asset/list/",onPostsLoaded,onPostsError);
        queue.add(request);
    }
    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>(){
        @Override
        public void onResponse(String response){
            //Deserializign the JSON
            //List<Post> posts = Array.asList(gson.fromJson(response, Post[].class));
           //register listview
            listview = (ListView) findViewById(R.id.listView_al);
            //1 usign gson to parse json
            Gson gson = new Gson();
           assetlist = (List<Asset>) gson.fromJson(response,Asset.class);
            //List<Assets>assetlist = (List<Assets>) gson.fromJson(response,Assets.class);


            //System.out.println(assetlist);
            //2 notify list adapter about data change/obtains list id to be use with AssetList
         // listadapter = new CustomListAdapter(this,Asset.class);//work on this
            listview.setAdapter(listadapter);

        }

    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener(){
      @Override
        public void onErrorResponse(VolleyError error){
          Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
      }
    };

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
