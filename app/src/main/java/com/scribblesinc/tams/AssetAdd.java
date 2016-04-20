package com.scribblesinc.tams;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.scribblesinc.tams.androidcustom.Item;
import com.scribblesinc.tams.androidcustom.MyAdapter;


import java.util.ArrayList;

public class AssetAdd extends AppCompatActivity {//AppCompatActivity



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //run parents method by extending the existing class or run this
        //class in addition to parent's class
        super.onCreate(savedInstanceState);
        //activity class creates window
        setContentView(R.layout.activity_asset_add);
        //Instantiating the toolbar of adding asset activity

       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });
           */
    //pass context and data to the custom adapter
       MyAdapter adapter = new MyAdapter(this, generateData());
        //Get ListView from content_asset_add
       final ListView listView = (ListView) findViewById(R.id.listView);
        //setListAdapter aka assign adapter to listview
        listView.setAdapter(adapter);

        //creating a contextmeny for listview
        registerForContextMenu(listView);

        //OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                //switch state to change accordingly based on user selection
                switch(position){
                    case 0: //camera
                        Intent newActivity_c = new Intent(AssetAdd.this,CameraCapture.class);
                        startActivity(newActivity_c);
                        break;
                    case 1://name
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 2://category
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 3://type
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 4://location
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 5://voice memo
                        Intent newActivity_a = new Intent(AssetAdd.this,AudioCapture.class);
                        startActivity(newActivity_a);
                        break;
                    case 6://description
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                }


            }
        });//end of OnItemClickListener


    }//end of onCreate


    private ArrayList<Item>generateData(){
        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name","Sharp Edged Sign"));
        items.add(new Item("Category","Road Signs"));
        items.add(new Item("Type","Caution Sign"));
        items.add(new Item("Location","Asset location"));
        items.add(new Item("Voice Memo","Record Voice Memo",R.drawable.ic_mic));
        items.add(new Item("Description","Notes"));

        items.add(new Item("Add","Adding another field"));
        items.add(new Item("Item","Description"));
        return items;

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_asset, menu);
        return true;
}



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if the back button is pressed
        if(item.getItemId() == android.R.id.home){
            finish(); //goes back to the previous activity
        }
        //when the filter button is pressed
        if(id == R.id.action_done){
            Toast.makeText(getApplicationContext(), "Not working ",
                    Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


}
