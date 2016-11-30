package com.scribblesinc.tams;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.ContextMenu;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.content.Context;
import android.provider.MediaStore.Images.Media;
import android.database.Cursor;


import android.graphics.drawable.BitmapDrawable;
import com.scribblesinc.tams.adapters.CustomAssetAdapter;
import com.scribblesinc.tams.androidcustom.Item;
import com.scribblesinc.tams.backendapi.Assets;

import java.io.File;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;





public class AssetAdd extends AppCompatActivity {//AppCompatActivity

    private CustomAssetAdapter adapter;
    private ListView listView;
    private Intent newActivity;

    private ImageView imageView;

    private static final int REQUEST_CAMERA = 200;//variable use for permission checking
    //Variables Constant to be use for intent across activites
    private static final String ARRAY_LIST = "com.scribblesinc.tams";//variable use for intent receivng of information
    private static final String REC_AUDIO = "com.scribblesinc.tams";
    private static final String ASSET_TITLE = "com.scribblesinc.tams";
    private static final String ASSET_NOTES = "com.scribblesinc.tams";

    //list of variables to be use for asset class
    private String assetName = null;
    private String assetDescription = null;
    private String assetCategory_Description= null;
    private String assetType = null;
    private Bitmap assetMedia_image =null;
    private String assetMedia_voice = null;
    private String convertedMedia_image = null;
    private Uri uriTest = null;
    private File fileTest = null;

    String testImagePath = null;


    //Variables to control context menu for Type, Category on long press hold on view for
    //type and category.
     private int isContextMenu = 0;
    private int CATEGORYPOSITION = 2;
    private int TYPEPOSITION= 3;


    //list of items that adapter will use to populate listview
    ArrayList<Item> items = new ArrayList<>();

    //Class for storing data inserted by user
    private Assets asset;
    //Declaring intent to be use
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* run parents method by extending the existing class or run this
        *  class in addition to parent's class*/
        super.onCreate(savedInstanceState);
        // activity class creates window
        if (savedInstanceState != null) {
            onRotate(savedInstanceState);

        }
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
        adapter = new CustomAssetAdapter(this, generateData());
        // Get ListView from content_asset_add
        listView = (ListView) findViewById(R.id.listView_aa);
        // setListAdapter aka assign adapter to listview
        listView.setAdapter(adapter);

        if(assetMedia_image != null){
            //this is kinda sloppy, but lets see if it works
            adapter.setBitMap(assetMedia_image);
            //setListAdapter aka assign adapter to listview
            listView.setAdapter(adapter);

        }


        //creating a contextmenu for listview
        this.registerForContextMenu(listView);

        //if activity is called via intent
        if(getCallingActivity() != null){
            //if activity is called via intent, update activity with information sent
            updateList();
        }





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
                        newActivity.putExtra(ASSET_TITLE,assetName);
                        startActivityForResult(newActivity,0);//upon return go t given requestCode
                        break;
                    case 2://category
                        isContextMenu = position;//int position to point if user selected type or category view
                        view.showContextMenu();
                        break;
                    case 3://type
                        isContextMenu = position;
                        view.showContextMenu();
                        break;
                    case 4://location
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id,Toast.LENGTH_LONG).show();
                        break;
                    case 5://voice memo
                         newActivity = new Intent(AssetAdd.this,AudioCapture.class);
                        //send assetMedia_voice to allow user to play or record a new audio
                        newActivity.putExtra(REC_AUDIO,assetMedia_voice);
                        startActivityForResult(newActivity,5);
                        break;
                    case 6://description
                        newActivity = new Intent(AssetAdd.this, NotesCapture.class);
                        newActivity.putExtra(ASSET_NOTES,assetDescription);
                        startActivityForResult(newActivity,6);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(),"Posi:"+position+"and"+"Id"+id+"not present",Toast.LENGTH_LONG).show();
                        break;
                }

                }
            }

        );//end of OnItemClickListener
    }//end of onCreate

    /*handle the permissions request response*/
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
        if(isContextMenu==CATEGORYPOSITION) { // menu shown based on type of view selected
            //contextual menu if catorogy view is selected
            getMenuInflater().inflate(R.menu.menu_context_category, menu);
        }else if(isContextMenu==TYPEPOSITION){
            getMenuInflater().inflate(R.menu.menu_context_type,menu);
        }
        //Reset value to zero to stop it from activating when user selects other views, not mentioned above
        isContextMenu = 0;
    }

    /* When the user selects a menu item  from contextmenu, the system calls
    *  this method so the appropriate action can be perform*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*Depending on user choice between category or type choice, changes are different*/
        if(isContextMenu==CATEGORYPOSITION) {
            //user click on category view
            if (id == R.id.stop_light) {
                assetCategory_Description = "stop light";
            }
            if (id == R.id.road_sign) {
                assetCategory_Description = "road sign";

            }

            adapter.getItem(2).setDescription(assetCategory_Description);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();

        }else if(isContextMenu==TYPEPOSITION){
            //in the type contextual menu
            if (id == R.id.caution_sign){
                assetType = "caution sign";
            }
            if (id == R.id.traffic_sign) {
                assetType = "traffic sign";
            }
            adapter.getItem(3).setDescription(assetType);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();

        }

        return true;
    }

    /* The list of items generated to be added to ListView*/
    private ArrayList<Item>generateData() {
       // ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name", assetName));
        items.add(new Item("AssetCategory",assetCategory_Description));
        items.add(new Item("Type",assetType));
        items.add(new Item("AssetLocation","Asset location"));
        items.add(new Item("Voice Memo","Record Voice Memo",R.drawable.ic_mic));
        items.add(new Item("Description", assetDescription));
        items.add(new Item(" "," "));//empty item to permit scrolling

        return items;
    }
  /*method use to update list when user click on a row view in assetList */
    private void updateList(){
        //Reading from intent AssetList when user wants to update or Delete asset
        Intent testIntent = getIntent();
        asset = (Assets) getIntent().getParcelableExtra(ARRAY_LIST);
        if(asset != null){
            //Store data
             assetName = asset.getName();
             assetDescription = asset.getDescription();
             assetCategory_Description= null;
             assetType = null;
             assetMedia_image = null;
             assetMedia_voice = asset.getMedia_voice_url();



            // populating activity's listview
            adapter.getItem(1).setDescription(asset.getName());
            adapter.getItem(6).setDescription(asset.getDescription());

            //update listview with new values.
            listView.setAdapter(adapter);

            this.registerForContextMenu(listView);

        }
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
            case 0://Name
                //gets the title from the key that was passed by the activity in TitleofAsset
                if(data !=null) {
                    assetName = data.getStringExtra(ASSET_TITLE);
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(1).setDescription(assetName);
                    adapter.notifyDataSetChanged();

                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listviewcu
                    this.registerForContextMenu(listView);
                }
                break;

            case 1://Image //requestCode for image is 1
             //  System.out.println("RQC: " + requestCode + " RC: " + resultCode);


                    Bundle extras = data.getExtras();
                    assetMedia_image = (Bitmap) extras.get("data");


                    //int width = listView.getWidth();
                    //int height = listView.getHeight();
                    //if(height>width){assetMedia_image = RotateBitmap(assetMedia_image, 90);}

                    convertedMedia_image = BitMapToString(assetMedia_image);

                    Uri testImageUri = data.getData();
                    String testImagePath = getRealPathFromURI(testImageUri);

                    System.out.println(testImagePath);
                    adapter.setPath(testImagePath);
                    //adapter.setBitMap(assetMedia_image);
                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listview
                    this.registerForContextMenu(listView);


                break;
            case 5://Audio recording
                if(data!=null)
                    //save data brought from the audio activity
                    assetMedia_voice = data.getStringExtra(REC_AUDIO);
                break;
            case 6://notes
                if (data != null) { // data can be null if back button is pressed!!!
                    //gets the title from the key that was passed by the activity in TitleofAsset
                    assetDescription = data.getStringExtra(ASSET_NOTES);
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(6).setDescription(assetDescription);
                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listview
                    this.registerForContextMenu(listView);
                }
                break;
            default://nothing
                Toast.makeText(getApplicationContext(), "View not on listview", Toast.LENGTH_LONG).show();
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("Name", assetName);
        savedInstanceState.putString("Description", assetDescription);
        savedInstanceState.putString("Type", assetType);
        savedInstanceState.putString("Category", assetCategory_Description);
        savedInstanceState.putString("Path", testImagePath);
        savedInstanceState.putString("Bitmap", convertedMedia_image);
        savedInstanceState.putString("Voice", assetMedia_voice);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRotate(Bundle savedInstanceState){
        assetName = savedInstanceState.getString("Name");
        assetDescription = savedInstanceState.getString("Description");
        assetType = savedInstanceState.getString("Type");
        assetCategory_Description = savedInstanceState.getString("Category");
        convertedMedia_image = savedInstanceState.getString("Bitmap");
        assetMedia_image = StringToBitMap(convertedMedia_image);
        assetMedia_voice = savedInstanceState.getString("Voice");
        testImagePath = savedInstanceState.getString("Path");
    }
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    /*
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    */

    public String getRealPathFromURI(Uri contentUri)
    {
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        catch (Exception e)
        {
            return contentUri.getPath();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
        *  automatically handle clicks on the Home/Up button, so long
        *  as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();
        // if the back button is pressed
        if(id == android.R.id.home) {
            finish(); //goes back to the previous activity
        }
        // when the filter button is pressed
        if(id == R.id.action_done) {
            Toast.makeText(getApplicationContext(), "Not working ", Toast.LENGTH_SHORT).show();
        }
        // resets the asset being created

        if(id == R.id.action_reset){
            Intent intent = new Intent(this, AssetAdd.class);
            startActivity(intent);
            finish(); // This opens a new AssetAdd and closes the current one.

        }

        return super.onOptionsItemSelected(item);
    }
}
