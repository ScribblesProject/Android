package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.fitness.request.ListClaimedBleDevicesRequest;
import com.scribblesinc.tams.adapters.CustomCategoryAdapter;
import com.scribblesinc.tams.backendapi.AssetCategory;

import java.util.ArrayList;

import static com.scribblesinc.tams.R.styleable.View;

/**
 * Created by Joel on 12/7/2016.
 */

public class ListCategory extends AppCompatActivity {
    private Intent intent; //intent for receiving and sending data
    private CustomCategoryAdapter categoryadapter;//adapter for displaying listview
    private ListView listview;//listview for this activity
    private static final String ASSET_CATEGORY = "com.scribblesinc.tams";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listcategory);
        // Instantiating the toolbar of adding asset activity thus allowing
        //activity to have menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // gets the action bar if not found
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //Get data sent from AssetAdd
        final ArrayList<AssetCategory> assetCategories = this.getIntent().getParcelableArrayListExtra(ASSET_CATEGORY);

        //instantiating and displaying listview
        categoryadapter = new CustomCategoryAdapter(this,assetCategories);
        listview = (ListView) findViewById(R.id.category_listview);
        listview.setAdapter(categoryadapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){

                Intent sendToPreviousActivity = new Intent();
                sendToPreviousActivity.putExtra(ASSET_CATEGORY,assetCategories.get(position));
                setResult(RESULT_OK,sendToPreviousActivity);//set data to be return to previous activity
                finish();//return
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);//menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //if back button is pressed
        if(id== android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}