package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.scribblesinc.tams.adapters.CustomTypeAdapter;
import com.scribblesinc.tams.backendapi.AssetCategory;
import com.scribblesinc.tams.backendapi.AssetType;

import java.util.ArrayList;

/**
 * Created by skenn on 12/7/2016.
 */

public class ListType extends AppCompatActivity  {
    private Intent intent;
    private ArrayList<AssetType> typeList;
    private static final String ASSET_TYPE = "com.scribblesinc.tams";
    private CustomTypeAdapter TypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listtype);
        // Instantiating the toolbar of adding asset activity thus allowing
        //activity to have menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // gets the action bar if not found
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //Get information set from AssetAdd
        //  intent = getIntent();
        //    categorylist = (ArrayList<AssetCategory>) intent.getParcelableExtra(ASSET_CATEGORY);
        //System.out.println( intent.getParcelableExtra(ASSET_CATEGORY));


        ArrayList<AssetType> assetTypes = this.getIntent().getParcelableArrayListExtra(ASSET_TYPE);
        Toast.makeText(getApplicationContext(), assetTypes.get(0).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);

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
