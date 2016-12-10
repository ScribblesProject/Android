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


import com.android.volley.Response;
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.backendapi.Assets;
import java.util.ArrayList;
import android.app.ProgressDialog;


/*
*INFORMATOIN: AssetList displays current Assets that the backend has, it uses Assets class to get the information
* arrayList for populating the listview. Note that most of the parsing is done by a call of the one of the functions in
* Assets not parsing is done by this class it just handles the final arrayList.
*
 */
public class AssetList extends AppCompatActivity {
    //Declaring the listAdapter and listview to be use
    private CustomListAdapter listAdapter;
    private ListView listview;
    //Declaring new intent to be initialize
    private Intent intent;
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

    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchAssets();

    }
    private void fetchAssets() {

        Assets.list(new Response.Listener<ArrayList<Assets>>() {
            @Override
            public void onResponse(final ArrayList<Assets> response) {

                if (response != null) {
                    //get view to be populated
                    listview = (ListView) findViewById(R.id.listView_al);
                    //create the adapter for listview based on response
                    listAdapter = new CustomListAdapter(AssetList.this, response);
                    //attach adapter to listview
                    listview.setAdapter(listAdapter);
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    //onclick listener to be use when user touches a view for more information,
                    //user then can update or delete view.
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                                                            //set the intent for passing arrayList data to AssetAdd
                                                            intent  = new Intent(AssetList.this,AssetAdd.class);
                                                            intent.putExtra(ARRAY_LIST, response.get(position));//just send the Asset selected
                                                            startActivityForResult(intent,1);//

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

        int id = item.getItemId();

        //if the AddAsset button is pressed
        if(id == R.id.action_add_asset){
            //
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