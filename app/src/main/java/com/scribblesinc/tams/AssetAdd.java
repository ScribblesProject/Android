package com.scribblesinc.tams;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.ContextMenu;
import android.os.Bundle;
import android.provider.MediaStore;
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

    private MyAdapter adapter;
    private ListView listView;
    private String title = "N/A";
    private String notes = "N/A";
    private boolean isType;
    private Intent newActivity;
    private static final int REQUEST_CAMERA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* run parents method by extending the existing class or run this
        *  class in addition to parent's class*/
        super.onCreate(savedInstanceState);
        // activity class creates window
        setContentView(R.layout.activity_asset_add);
        // Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //gets action bar that's supported if null
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // pass context and data to the custom adapter
        adapter = new MyAdapter(this, generateData());
        // Get ListView from content_asset_add
        listView = (ListView) findViewById(R.id.listView);
        // setListAdapter aka assign adapter to listview
        listView.setAdapter(adapter);


        //creating a contextmenu for listview
        this.registerForContextMenu(listView);

        //OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

                //switch state to change accordingly based on user selection
                switch(position){
                    case 0: //camera
                        //do permission checking
                            ActivityCompat.requestPermissions(AssetAdd.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                        break;
                    case 1://name
                        newActivity = new Intent(AssetAdd.this, TitleofAsset.class);
                        startActivityForResult(newActivity,2);
                        //Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 2://category
                        isType = false;
                        view.showContextMenu();
                        //Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 3://type
                        isType = true;
                        view.showContextMenu();
                        //Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 4://location
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 5://voice memo
                         newActivity = new Intent(AssetAdd.this,AudioCapture.class);
                        startActivityForResult(newActivity,5);
                        //startActivity for result will be implemented later  to handle info
                        break;
                    case 6://description
                        newActivity = new Intent(AssetAdd.this, NotesCapture.class);
                        startActivityForResult(newActivity,6);
                        //Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                }

                }
            }
        );//end of OnItemClickListener
    }//end of onCreate
    //handle the permissions request response
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults){

        //start audio recording
        if(requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "App has microphone capturing permission", Toast.LENGTH_LONG).show();
                newActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(newActivity,1);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AssetAdd.this, Manifest.permission.CAMERA)) {
                    //explain user need of permission
                    Toast.makeText(getApplicationContext(),"Audio Capturing Permission Required", Toast.LENGTH_LONG).show();
                } else {
                    //Don't ask again for permission, handle rest of app without this permisson
                    finish();
                    Toast.makeText(getApplicationContext(), "App has no audio capturing permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }//end of onRequestPermissoinResult

    /*onCreateContextMenu, responsible for creating contextual menus for type item and category,
    * based on a flag the according menu will be shown
    * */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo) {
        super.onCreateContextMenu(menu,v,menuinfo);
        if(isType) { // item selected is the type view
            getMenuInflater().inflate(R.menu.menu_context_type,menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_context_category, menu);
        }
    }

    /* When the user selects a menu item  from contextmenu, the system calls
    *  this method so the appropriate action can be perform*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(isType) {
            Toast.makeText(getApplicationContext(),"isType",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"is category",Toast.LENGTH_LONG).show();
        }

        return true;
    }

    // The array list of items to be added to my ListView
    private ArrayList<Item>generateData() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name", title));
        items.add(new Item("Category","Road Signs"));
        items.add(new Item("Type","Caution Sign"));
        items.add(new Item("Location","Asset location"));
        items.add(new Item("Voice Memo","Record Voice Memo",R.drawable.ic_mic));
        items.add(new Item("Description", notes));
        items.add(new Item(" "," "));//empty item to permit scrolling
        return items;
    }

    /* Start activityForResult() will call the intent to my photo, notes and audio, whatever is
    *  sent back is receive here onActivityResult(), thus here we process result to be outputted on
    *  addAdd viewlist*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // result of each view in list will come here and here it will be handled
        // accordingly
        // Note: Case statement numbers cause conflicts. In "Name" typing something in and pressing done gives us REQ = 1, RES = -1, and
        // ID = {(has extras)}, while pressing back (on both app and phones gives) 1, 0, and null respectively
        switch (requestCode) {
            case 0:
                Toast.makeText(getApplicationContext(), "Picture to be handled", Toast.LENGTH_LONG).show();
                break;
            case 1:
                System.out.println("RQC: " + requestCode + " RC: " + resultCode);

                if (data != null) { // data can be null if back button is pressed!!!
                     //gets the title from the key that was passed by the activity in TitleofAsset
                    title = data.getStringExtra("assetTitle");
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(1).setDescription(title);
                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listview
                    this.registerForContextMenu(listView);
                }

                break;
            case 5:
                Toast.makeText(getApplicationContext(), "Audio to be handled", Toast.LENGTH_LONG).show();
                break;
            case 6:
                if (data != null) { // data can be null if back button is pressed!!!
                    //gets the title from the key that was passed by the activity in TitleofAsset
                    notes = data.getStringExtra("assetNotes");
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(6).setDescription(notes);
                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listview
                    this.registerForContextMenu(listView);
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "something else", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_asset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
        *  automatically handle clicks on the Home/Up button, so long
        *  as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();
        // if the back button is pressed
        if (item.getItemId() == android.R.id.home) {
            finish(); //goes back to the previous activity
        }
        // when the filter button is pressed
        if (id == R.id.action_done) {
            Toast.makeText(getApplicationContext(), "Not working ", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
