package com.scribblesinc.tams;

import android.content.Intent;
import android.content.Context;
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

import com.google.android.gms.appindexing.AndroidAppUri;
import com.scribblesinc.tams.androidcustom.Item;
import com.scribblesinc.tams.androidcustom.MyAdapter;


import java.util.ArrayList;

public class AssetAdd extends AppCompatActivity {//AppCompatActivity
   private boolean isType;


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

    //pass context and data to the custom adapter
       MyAdapter adapter = new MyAdapter(this, generateData());
        //Get ListView from content_asset_add
       final ListView listView = (ListView) findViewById(R.id.listView);
        //setListAdapter aka assign adapter to listview
        listView.setAdapter(adapter);

        //creating a contextmeny for listview
        this.registerForContextMenu(listView);

        //OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Intent newActivity;
                //switch state to change accordingly based on user selection
                switch(position){
                    case 0: //camera
                        //Note we have to have an if statement cause API
                        //cant seem to work with ACTION_IMAGE_CAPTURE_SECURE
                        //but yes with the one below
                        
                        newActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                        startActivityForResult(newActivity,1);
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
                        //startActivity for result will be implemented later  to handle info
                        startActivityForResult(newActivity,5);
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
        });//end of OnItemClickListener

    }//end of onCreate


    /*onCreateContextMenu, responsible for creating contextual menus for type item and category,
    * based on a flag the according menu will be shown
    * */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuinfo){
        super.onCreateContextMenu(menu,v,menuinfo);
        if(isType){//item selected is the type view
            getMenuInflater().inflate(R.menu.menu_context_type,menu);
        }else {
            getMenuInflater().inflate(R.menu.menu_context_category, menu);
        }
    }
    //When the user selects a menu item  from contextmenu, the system calls
    //this method so the appropriate action can be perform
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(isType){
            Toast.makeText(getApplicationContext(),"isType",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),"is category",Toast.LENGTH_LONG).show();
        }

        return true;
    }


 //The array list of items to be added to my ListView
    private ArrayList<Item>generateData(){
        ArrayList<Item> items = new ArrayList<>();

        items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name","Sharp Edged Sign"));
        items.add(new Item("Category","Road Signs"));
        items.add(new Item("Type","Caution Sign"));
        items.add(new Item("Location","Asset location"));
        items.add(new Item("Voice Memo","Record Voice Memo",R.drawable.ic_mic));
        items.add(new Item("Description","Notes"));
        items.add(new Item(" "," "));//empty item to permit scrolling
        return items;

    }

    /*Start activityForResult() will call the intent to my photo, notes and audio, whatever is
    * sent back is receive here onActivityResult(), thus here we process result to be outputted on
    * addAdd viewlist*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Toast.makeText(getApplicationContext(),"Picture is here",Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Name is here", Toast.LENGTH_LONG).show();
                //adapter.getItem(requestCode).colorchangeType = change;
                //adapter.notifysetdatachange();
                //TextView thistextview = (TextView)findViewById(R.id.)
                break;
            case 5:
                Toast.makeText(getApplicationContext(), "Audio is here", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(getApplicationContext(), "Notes is here", Toast.LENGTH_LONG).show();
                //Result string is brought back, but dont know yet how to access id-view
                //of content in the listview to change string
                //  String newNotes = data.getStringExtra("NOTES");
                //textNotes.setText(newNotes);

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
