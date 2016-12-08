package com.scribblesinc.tams;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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
import android.provider.MediaStore.Images.Media;
import android.graphics.drawable.BitmapDrawable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.scribblesinc.tams.adapters.CustomAssetAdapter;
import com.scribblesinc.tams.adapters.CustomListAdapter;
import com.scribblesinc.tams.androidcustom.Item;
import com.scribblesinc.tams.backendapi.AssetCategory;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.AssetType;
import com.scribblesinc.tams.backendapi.Assets;
import com.scribblesinc.tams.network.HttpJSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Bitmap.createScaledBitmap;


public class AssetAdd extends AppCompatActivity {

    private CustomAssetAdapter adapter;
    private ListView listView;
    private Intent newActivity;

    private ImageView imageView;

    private static final int REQUEST_CAMERA = 200;//variable use for permission checking
    //Variables Constant to be use for handling data across information
    private static final String ARRAY_LIST = "com.scribblesinc.tams";//variable use for intent receivng of information
    private static final String REC_AUDIO = "com.scribblesinc.tams";
    private static final String ASSET_TITLE = "com.scribblesinc.tams";
    private static final String ASSET_NOTES = "com.scribblesinc.tams";
    private static final String ASSET_CATEGORY = "com.scribblesinc.tams";
    private static final String ASSET_TYPE = "com.scribblesinc.tams";

    public static final String ASSET_LOCATION = "com.scribblesinc.tams";

    //list of variables to be use for asset class

    // NOTE: THIS INFO IS ALREADY CONTAINED IN asset variable. NO NEED TO STORE IT AGAIN
    // JUST UPDATE THE ASSET CLASS AS NEEDED, WHEN READY TO COMMIT CHANGES CALL asset.update(...)

    private Bitmap assetMedia_image = null;
    private String assetMedia_voice = null;

    //Variables to control context menu for Type, Category on long press hold on view for
    //type and category.
    private boolean isContextMenu;

    private int CATEGORYPOSITION = 2;//position of category
    private int TYPEPOSITION = 3;//position of Type
    private long CatID = 0;
    private boolean isAssetExist = false;//if user is looking at an existing
    private boolean isType;
    //storing if any of the fields has been modified
    private boolean isDescriptionModified = false;
    private boolean isImageModified = false;
    private boolean isAudioModified = false;
    private boolean isTitleModified = false;

    /*
    * Update:
    * onCreate - put the current asset info in there. (take info out of asset class)
    * when you have the categ/type list open, and the user taps +
    *     replace the value in these variables with the new value that the user typed
    * Change onAssetUpdate/onAssetCreate to pull from these variables
    * */
    private String assetCategory = "";
    private String assetCategoryDescription = "";
    private String assetType = "";
    private String convertedBitmap;
    private String categoryDescription;
    private String Type;
    private Bitmap scaledMedia;

    //list of items that adapter will use to populate listview
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<LatLng> asset_locations = null;

    //Class for storing data inserted by user
    private Assets asset;
    private AssetCategory assetcategory;

    //Declaring intent to be use
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* run parents method by extending the existing class or run this
        *  class in addition to parent's class*/
        asset_locations = new ArrayList<>();
        super.onCreate(savedInstanceState);
        // activity class creates window
        setContentView(R.layout.activity_asset_add);
        // Instantiating the toolbar of adding asset activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //gets action bar that's supported if null
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // pass context and data to the custom adapter
        adapter = new CustomAssetAdapter(this, generateData());
        // Get ListView from content_asset_add
        listView = (ListView) findViewById(R.id.listView_aa);
        //this restores the stuff in case phone is rotated.
        if (savedInstanceState != null) {
            onRotate(savedInstanceState);


        }
        if(assetMedia_image != null){
            adapter.setBitMap(assetMedia_image);
        }


        // setListAdapter aka assign adapter to listview
        listView.setAdapter(adapter);


        //register Listview for Context menu
        this.registerForContextMenu(listView);

        //if activity is called via intent
        if (getCallingActivity() != null) {
            isAssetExist = true;
            //if activity is called via intent, update activity with information sent
            updateList();
        }

        //OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                                                //switch state to change accordingly based on user selection
                                                switch (position) {
                                                    case 0: //camera
                                                        //do permission checking
                                                        ActivityCompat.requestPermissions(AssetAdd.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                                                        break;
                                                    case 1://name
                                                        newActivity = new Intent(AssetAdd.this, TitleofAsset.class);
                                                        newActivity.putExtra(ASSET_TITLE, adapter.getItem(1).getDescription());
                                                        startActivityForResult(newActivity, 0);//upon return go t given requestCode
                                                        break;
                                                    case 2://category

                                                        listCategory();//call function to create activity for category

                                                       // newActivity = new Intent(AssetAdd.this, ListCategory.class);
                                                        //pass arraylist category
                                                       // newActivity.putExtra(ASSET_CATEGORY,adapter.getItem(2).getDescription());
                                                        //startActivityForResult(newActivity,2);
                                                        //view.showContextMenu();
                                                        break;
                                                    case 3://type
                                                        if(CatID != 0) {
                                                            listType(CatID);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Use Category First", Toast.LENGTH_LONG).show();
                                                        }
                                                        break;
                                                    case 4://location
                                                        //Toast.makeText(getApplicationContext(), "Posi:" + position + "and" + "Id" + id, Toast.LENGTH_LONG).show();
                                                        newActivity = new Intent(AssetAdd.this, SelectLocation.class);
                                                        newActivity.putParcelableArrayListExtra(ASSET_LOCATION, asset_locations);
                                                        startActivityForResult(newActivity,4);
                                                        //newActivity.putExtra(ASSET_LOCATION, asset_locations);
                                                        //startActivityForResult(newActivity,4);

                                                        //make map view intent with flag or create new map view activity
                                                        //newActivity = new Intent(getApplicationContext(), SelectLocation.class);
                                                        //startActivity(newActivity);

                                                        break;
                                                    case 5://voice memo
                                                        newActivity = new Intent(AssetAdd.this, AudioCapture.class);
                                                        //send assetMedia_voice to allow user to play or record a new audio
                                                        newActivity.putExtra(REC_AUDIO, assetMedia_voice);
                                                        startActivityForResult(newActivity, 5);
                                                        break;
                                                    case 6://description
                                                        newActivity = new Intent(AssetAdd.this, NotesCapture.class);
                                                        newActivity.putExtra(ASSET_NOTES, adapter.getItem(6).getDescription());
                                                        startActivityForResult(newActivity, 6);
                                                        break;
                                                    default:
                                                        Toast.makeText(getApplicationContext(), "Posi:" + position + "and" + "Id" + id + "not present", Toast.LENGTH_LONG).show();
                                                        break;
                                                }

                                            }
                                        }

        );//end of OnItemClickListener
    }//end of onCreate

    /*handle the permissions request response*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //start audio recording
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "App has microphone capturing permission", Toast.LENGTH_LONG).show();
                newActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(newActivity, 1);
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(AssetAdd.this, Manifest.permission.CAMERA)) {
                    //explain user need of permission
                    Toast.makeText(getApplicationContext(), "Audio Capturing Permission Required", Toast.LENGTH_LONG).show();

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
        super.onCreateContextMenu(menu, v, menuinfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuinfo;
        //Create a context menu only when one of these two view positions are press
        final ContextMenu thismenu = menu;
        final View thisview = v;
        if (info.position == CATEGORYPOSITION) { // menu shown based on type of view selected
            //contextual menu if catorogy view is selected
            // getMenuInflater().inflate(R.menu.menu_context_category, menu);

            //create context menu
           //---- listCategory(menu,v)L CONTXTEXT MENU WILL BE REMOVED.
            /*
            Toast.makeText(getApplicationContext(), "am in listCategory!", Toast.LENGTH_LONG).show();
            menu.setHeaderTitle("Test");
            menu.add(0,v.getId(),0,"one");
            menu.add(0,v.getId(),0,"two");
            */


            //maybe a for loop
            isType = false;
        }
        if(info.position==TYPEPOSITION){
            getMenuInflater().inflate(R.menu.menu_context_type, menu);
            isType = true;
        }
    }

    /* When the user selects a menu item  from contextmenu, the system calls
        *  this method so the appropriate action can be perform*/
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*Depending on user choice between category or type choice, changes are different*/
        if (!isType) {

            /*
            Toast.makeText(getApplicationContext(), "Category", Toast.LENGTH_LONG).show();
            //user click on category view
            String newCategory = null;
            if (id == R.id.stop_light) {
                newCategory = "stop light";
            }
            if (id == R.id.road_sign) {
                newCategory = "road sign";
            }
            adapter.getItem(2).setDescription(newCategory);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();
            */

        } else  {
            Toast.makeText(getApplicationContext(), "TYPE", Toast.LENGTH_LONG).show();
            String newType = null;

            //in the type contextual menu
            if (id == R.id.caution_sign) {
                newType = "caution sign";
            }
            if (id == R.id.traffic_sign) {
                newType = "traffic sign";
            }

            adapter.getItem(3).setDescription(newType);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();

        }

        return true;
    }

    /* The list of items generated to be added to ListView*/
    private ArrayList<Item> generateData() {

        // Data populated onCreate in updateList

        // ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Image", "Image of Asset", R.drawable.ic_camera_alt));
        items.add(new Item("Name", null));
        items.add(new Item("AssetCategory", null));
        items.add(new Item("Type", null));
        items.add(new Item("Asset Location", "Asset location"));
        items.add(new Item("Voice Memo", "Record Voice Memo", R.drawable.ic_mic));
        items.add(new Item("Description", null));
        //items.add(new Item(" ", " "));//empty item to permit scrolling

        return items;
    }

    /*method use to update list when user click on a row view in assetList */
    private void updateList() {
        //Reading from intent AssetList when user wants to update or Delete asset
        Intent testIntent = getIntent();
        asset = (Assets) getIntent().getParcelableExtra(ARRAY_LIST);
        Map<String, AssetLocation> asset_locs;// = new HashMap<String, AssetLocation>();
        //ArrayList<AssetLocation> asset_locs;
        if (asset != null) {
            //Store data
            assetMedia_image = null;
            assetMedia_voice = asset.getMedia_voice_url();

            // populating activity's listview
            adapter.getItem(1).setDescription(asset.getName());
            adapter.getItem(2).setDescription(asset.getCategory());
            adapter.getItem(3).setDescription(asset.getAsset_type());
            asset_locs = asset.getLocations();
            //Toast.makeText(getApplicationContext(), asset_locs.toString(),
                    //Toast.LENGTH_SHORT).show();

            if(asset_locs != null) {
                asset_locations = new ArrayList<>();
                for (String key : asset_locs.keySet()) {
                    double lat = asset_locs.get(key).getLatitude();
                    double lon = asset_locs.get(key).getLongitude();
                    LatLng newLatLng = new LatLng(lat, lon);
                    asset_locations.add(newLatLng);
                }
                adapter.getItem(4).setDescription("" + asset_locations.size() + " selected");
            }else{
                adapter.getItem(4).setDescription("0 selected");
            }

            /**if(asset_locs != null){
                if(asset_locs.size() > 0){
                    for(int i = 0; i < asset_locs.size(); i++){
                        asset_locs.
                    }
                }
            }*/
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
                if (data != null) {
                    //check if description has been modified
                    if(adapter.getItem(1).getDescription() != null) {//if description is not null
                        if (adapter.getItem(1).getDescription().compareTo(data.getStringExtra(ASSET_TITLE)) != 0) {//data has been modified
                            this.isTitleModified = true;
                        }
                    }

                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(1).setDescription(data.getStringExtra(ASSET_TITLE));
                    adapter.notifyDataSetChanged();

                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listviewcu
                    this.registerForContextMenu(listView);
                }
                break;

            case 1://Image //requestCode for image is 1

                if(data != null) {
                    Bundle extras = data.getExtras();
                    //get thumbnail from data
                    assetMedia_image = (Bitmap) extras.get("data");


                    //time to rotate if needed.
                    int width = listView.getWidth();
                    int height = listView.getHeight();
                    if(height>width){
                        assetMedia_image = RotateBitmap(assetMedia_image, 90);
                    }



                    //give adapter bitmap.
                    adapter.setBitMap(assetMedia_image);
                    convertedBitmap = BitMapToString(assetMedia_image);

                    //here we get the full sized image for later
                    Uri testImageUri = data.getData();
                    String testImagePath = getRealPathFromURI(testImageUri);
                    Bitmap fullrezMedia = BitmapFactory.decodeFile(testImagePath);

                    if(height>width) {
                        scaledMedia = createScaledBitmap(fullrezMedia, 800, 550, false);
                    }else{
                        scaledMedia = createScaledBitmap(fullrezMedia, 550, 800, false);
                    }

                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listview
                    this.registerForContextMenu(listView);
                }

                break;
            case 2:
                if(data != null){
                    //Category picked is here
                    AssetCategory assetcategory;
                     assetcategory = (AssetCategory) data.getParcelableExtra(ASSET_CATEGORY);
                    //set adapter
                    adapter.getItem(2).setDescription(assetcategory.getName());
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    this.registerForContextMenu(listView);
                    //set global id
                    CatID = assetcategory.getId();
                }

                break;


            case 3:
                if(data != null){

                    AssetType assettype;
                    assettype = (AssetType) data.getParcelableExtra(ASSET_TYPE);


                    adapter.getItem(3).setDescription(assettype.getName());
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    this.registerForContextMenu(listView);




                    //gets the item at index 1 (the description of the title) and changes it
                    //adapter.getItem(2).setDescription(assetcategory.getName());
                    //adapter.notifyDataSetChanged();

                    //setListAdapter aka assign adapter to listview
                    listView.setAdapter(adapter);
                    //creating a contextmeny for listviewcu
                    this.registerForContextMenu(listView);

                }


                break;
            case 4: //location
                if(data != null){
                    if(adapter.getItem(4).getDescription() != null){
                        asset_locations = data.getParcelableArrayListExtra(ASSET_LOCATION);
                        if(asset_locations == null){
                            adapter.getItem(4).setDescription("0 selected");
                        }else{
                            String asset_str = "" + asset_locations.size() + " selected";
                            //gets the item at index 4 (the description of the title) and changes it
                            adapter.getItem(4).setDescription(asset_str);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;
            case 5://Audio recording
                if (data != null)
                    //save data brought from the audio activity
                    assetMedia_voice = data.getStringExtra(REC_AUDIO);
                break;
            case 6://notes
                if (data != null) { // data can be null if back button is pressed!!!

                    //check if description has been modified
                    if(adapter.getItem(6).getDescription() != null) {//if description is not null
                        if (adapter.getItem(6).getDescription().compareTo(data.getStringExtra(ASSET_NOTES)) != 0) {
                            this.isDescriptionModified = true;//data has been modified
                        }
                    }

                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(6).setDescription(data.getStringExtra(ASSET_NOTES));
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
        if (!isAssetExist) {
            getMenuInflater().inflate(R.menu.menu_add_asset, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_asset_modify, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Handle action bar item clicks here. The action bar will
        *  automatically handle clicks on the Home/Up button, so long
        *  as you specify a parent activity in AndroidManifest.xml.*/
        int id = item.getItemId();
        // if the back button is pressed
        if (id == android.R.id.home) {
            finish(); //goes back to the previous activity
        }
        // when the filter button is pressed
        if (id == R.id.action_create) {
            assetToCreate();
        }
        // resets the asset being created

        if (id == R.id.action_reset) {

            Intent intent = new Intent(this, AssetAdd.class);
            startActivity(intent);
            finish(); // This opens a new AssetAdd and closes the current one.

        }
        if (id == R.id.action_update) {
            assetToUpdate();
        }
        if (id == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmationAlertDialogStyle);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {
                    assetToDelete();


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int choice) {

                }
            });
            builder.setMessage("Selecting 'Ok' will delete this asset! ");
            builder.setTitle("Attemping to Delete Asset");

            //Get the AlertDialog from create();
            AlertDialog d = builder.create();
            d.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //methods category and type implementation
    public  void listCategory() {

        AssetCategory.list(new Response.Listener<ArrayList<AssetCategory>>() {
            public void onResponse(final ArrayList<AssetCategory>response){
                if(response !=null){
                    //ORIGNALToast.makeText(getApplicationContext(),response.get(1).toString() ,Toast.LENGTH_LONG).show();
                    newActivity = new Intent(AssetAdd.this,ListCategory.class);

                    newActivity.putParcelableArrayListExtra(ASSET_CATEGORY,response);
                  // startActivity(newActivity);
                    startActivityForResult(newActivity,2);

                }
            }
        },null);


    }
    public void listType(long id){
        AssetType.list(id, new Response.Listener<ArrayList<AssetType>>() {
            @Override
            public void onResponse(ArrayList<AssetType> response) {
                if(response != null){
                    System.out.println("listType success, Response is :" + response);

                    newActivity = new Intent(AssetAdd.this,ListType.class);

                    newActivity.putParcelableArrayListExtra(ASSET_TYPE,response);

                    startActivityForResult(newActivity,3);
                }
            }
        },null);


    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        try {
            //String[] proj = {Media.DATA};
            //Cursor cursor = managedQuery(contentUri, proj, null, null, null);
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putString("Name", adapter.getItem(1).getDescription());
        savedInstanceState.putString("Description", adapter.getItem(6).getDescription());
        savedInstanceState.putString("Type", adapter.getItem(3).getDescription());
        savedInstanceState.putString("Category", adapter.getItem(2).getDescription());
        savedInstanceState.putString("Bitmap", convertedBitmap);

        savedInstanceState.putString("Voice", assetMedia_voice);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRotate(Bundle savedInstanceState) {
        adapter.getItem(1).setDescription(savedInstanceState.getString("Name"));
        adapter.getItem(6).setDescription(savedInstanceState.getString("Description"));
        adapter.getItem(2).setDescription(savedInstanceState.getString("Category"));
        adapter.getItem(3).setDescription(savedInstanceState.getString("Type"));

        convertedBitmap = savedInstanceState.getString("Bitmap");
        assetMedia_image = StringToBitMap(convertedBitmap);
        assetMedia_voice = savedInstanceState.getString("Voice");


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //methods to delete and update asset
    public void assetToDelete() {

        if (asset == null) {
            Toast.makeText(getApplicationContext(), "Error! Asset Does Not Exist!", Toast.LENGTH_LONG).show();
            return;
        }

        asset.delete(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                //SUCCESS!!!
                Toast.makeText(getApplicationContext(), "ASSET DELETED SUCCESSFULLY!!!!!!!", Toast.LENGTH_LONG).show();
                dismissView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage = "Error Deleting Asset! Message: " + error.getMessage();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void assetToCreate() {
        //Pull info from fields
        String name = adapter.getItem(1).getDescription();
        String category = assetCategory;//adapter.getItem(2).getDescription();
        String categoryDescription = assetCategoryDescription;
        String typeName = assetType;//adapter.getItem(3).getDescription();
        String description = adapter.getItem(6).getDescription();
        ArrayList<AssetLocation> locations = new ArrayList<AssetLocation>();

        for(int i = 0; i < asset_locations.size(); i++){
            LatLng loc = asset_locations.get(i);
            double lat = loc.latitude;
            double lon = loc.longitude;
            AssetLocation assets_loc = new AssetLocation(lat,lon);
            locations.add(assets_loc);
        }

        //Validate
        if (name == null || description == null) {
            Toast.makeText(getApplicationContext(), "ERROR - name or description is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        //Set default values for null
        category = (category != null) ? category : "uncategorized";
        categoryDescription = (categoryDescription != null) ? categoryDescription : "na";
        typeName = (typeName != null) ? typeName : "uncategorized";


        Assets.create(name, description, category, categoryDescription, typeName, locations, new Response.Listener<Long>() {
            @Override
            public void onResponse(Long newAssetId) {
                if (newAssetId > 0) {
                    //SUCCESS
                    Toast.makeText(getApplicationContext(), "SUCCESS!!", Toast.LENGTH_LONG).show();
                    dismissView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Unable to create asset! " + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void assetToUpdate() {

        if (asset == null) {
            Toast.makeText(getApplicationContext(), "Error! Asset Does Not Exist!", Toast.LENGTH_LONG).show();
            return;
        }

        //Pull info from fields
        String name = adapter.getItem(1).getDescription();
        String category = adapter.getItem(2).getDescription();
        String categoryDescription = null;
        String typeName = adapter.getItem(3).getDescription();
        String description = adapter.getItem(6).getDescription();
        Map<String, AssetLocation> locations = new HashMap<String, AssetLocation>();


        //Validate
        if (name == null || description == null) {
            Toast.makeText(getApplicationContext(), "ERROR - name or description is empty.", Toast.LENGTH_LONG).show();
            return;
        }

        //Set default values for null
        category = (category != null) ? category : "uncategorized";
        categoryDescription = (categoryDescription != null) ? categoryDescription : "na";
        typeName = (typeName != null) ? typeName : "uncategorized";

        //Pull info out of fields
        asset.setName(name);
        asset.setCategory(category);
        asset.setAsset_type( typeName );
        asset.setCategory_description(categoryDescription);
        asset.setDescription( description );
        asset.setLocations(locations);

        //Update the asset
        asset.update(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean success) {
                if (success)
                {
                    //Upload image & voice. Progress Updates & Errors handled in respective functions.
                    updateAssetMedia(new Response.Listener<Boolean>() {
                        @Override
                        public void onResponse(Boolean success) {
                            if (success)
                            {
                                Toast.makeText(getApplicationContext(), "SUCCESS!!!", Toast.LENGTH_LONG).show();
                                dismissView();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to update asset! An unknown error occurred!", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //ERROR!!!!
                String errorMessage = "Error Updating Asset! Message: " + error.getMessage();
                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }

    //Error Messages produced in individual
    public void updateAssetMedia(final Response.Listener<Boolean> listener)
    {
        uploadImageIfNeeded(new Response.Listener<Boolean>() {
            @Override
            public void onResponse(final Boolean imageSuccess) {

                uploadMemoIfNeeded(new Response.Listener<Boolean>() {
                    @Override
                    public void onResponse(Boolean memoSuccess) {

                        if (listener != null) {
                            listener.onResponse( imageSuccess && memoSuccess );
                        }
                    }
                });
            }
        });
    }

    public void uploadImageIfNeeded(final Response.Listener<Boolean> listener)
    {
        if (imageWasModified())
        {
            //NOTE: The attachImage method hasnt been implemented. Lets work on this together.
            // We can change this parameter type to bitmap or w/e.. so long as I can get it into raw bytes.

            asset.attachImage(scaledMedia, new Response.Listener<Double>() {
                @Override
                public void onResponse(Double progress) {
                    //UPDATE PROGRESS BAR
                }
            }, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean success) {
                    if (!success) {
                        String errorMessage = "Error Uploading Asset Image! Unknown Error";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }

                    if (listener != null) {
                        listener.onResponse(success);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = "Error Uploading Asset Image! Message: " + error.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                    if (listener != null) {
                        listener.onResponse(false);
                    }
                }
            });
        }
        else {
            //If no modifications, success.
            if (listener != null) {
                listener.onResponse(true);
            }
        }
    }

    public void uploadMemoIfNeeded(final Response.Listener<Boolean> listener)
    {
        if (memoWasModified())
        {
            //NOTE: The attachMemo method hasnt been implemented. Lets work on this together.

            asset.attachVoiceMemo(assetMedia_voice, new Response.Listener<Double>() {
                @Override
                public void onResponse(Double progress) {
                    //UPDATE PROGRESS BAR
                }
            }, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean success) {
                    if (!success) {
                        String errorMessage = "Error Uploading Asset Memo! Unknown Error";
                        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }

                    if (listener != null) {
                        listener.onResponse(success);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage = "Error Uploading Asset Memo! Message: " + error.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();

                    if (listener != null) {
                        listener.onResponse(false);
                    }
                }
            });
        }
        else {
            if (listener != null) {
                listener.onResponse(true);
            }
        }
    }

    public boolean titleWasModified(){
        if(!isTitleModified) return false;
        else return true;
    }
    public boolean audioWasModified(){
        if(!isAudioModified) return false;
        else return false;
    }

    //check if data has been modified
    public boolean memoWasModified()
    {
        if(!isDescriptionModified)return false;
        else return true;
    }

    //check is data has been modified--WORK IN PROGESS
    public boolean imageWasModified()
    {
        if(!isImageModified) return false;
        else return false;
    }

    public void dismissView()
    {
        this.finish();
    }
}