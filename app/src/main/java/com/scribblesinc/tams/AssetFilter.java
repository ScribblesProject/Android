package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AssetFilter extends AppCompatActivity {
    String[] categoryTypeArray = {"Asset Category", "Asset Type"};
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //gets action bar that's supported if null
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //Used to display the category and type listview entries
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.asset_filter_listview, categoryTypeArray);

        ListView listView = (ListView) findViewById(R.id.cat_types);
        listView.setAdapter(adapter);

        //Listener to determine which options gets tapped
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0: //when Asset Category is tapped
                        //Toast.makeText(getApplicationContext(), categoryTypeArray[position], Toast.LENGTH_LONG).show();
                        intent = new Intent(getApplicationContext(), AssetFilterCategories.class);
                        startActivity(intent);
                        break;
                    case 1: //when Asset Type is tapped
                        Toast.makeText(getApplicationContext(), categoryTypeArray[position], Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //when the edit button is pressed
        if(id == R.id.action_clear){
            Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();
        }

        //if the add asset button is pressed
        if(id == R.id.action_apply_filter){
            Toast.makeText(getApplicationContext(), "Not Working Yet",
                    Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, AssetAdd.class);
            //startActivity(intent);
        }

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asset_filter, menu);
        return true;
    }

}
