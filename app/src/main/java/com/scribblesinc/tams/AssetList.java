package com.scribblesinc.tams;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.backendapi.Assets;
import java.util.ArrayList;


public class AssetList extends AppCompatActivity {
    //Declaring the listAdapter and listview to be use
    private CustomListAdapter listAdapter;
    private ListView listview;
    //Declaring new activity to be initialize
    private Intent newActivity;
    static final String ARRAY_LIST = "com.scribblesinc.tams";


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
    }

    private void fetchAssets() {
        Assets.list(new Response.Listener<ArrayList<Assets>>() {
            @Override
            public void onResponse(final ArrayList<Assets> response) {
                if (response != null) {
                    //get view to be populated
                    listview = (ListView) findViewById(R.id.listView_al);
                    //create the adapter for listview
                    listAdapter = new CustomListAdapter(AssetList.this, response);
                    //attach adapter to listview
                    listview.setAdapter(listAdapter);

                    //listAdapter.notifyDataSetChanged();
                    //onclick listener to be use when user touches a view for more information,
                    //user then can update or delete view.
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                                                            newActivity  = new Intent(AssetList.this,AssetAdd.class);
                                                           //put data on intent -work in progress
                                                            //newActivity.putExtra(ARRAY_LIST,response);

                                                            startActivityForResult(newActivity,1);
                                                            //Toast.makeText(getApplicationContext(), "Ping ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                    );
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