package com.scribblesinc.tams;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
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
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Media;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.scribblesinc.tams.adapters.CustomAssetAdapter;
import com.scribblesinc.tams.backendapi.AssetCategory;
import com.scribblesinc.tams.backendapi.AssetLocation;
import com.scribblesinc.tams.backendapi.AssetType;
import com.scribblesinc.tams.backendapi.Assets;
import com.scribblesinc.tams.util.Item;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/*
* Class info: This class handles displaying, viewing of a new or existing asset while also takes care of updating and
* deleting of a new asset.
*
* */
public class AssetAdd extends AppCompatActivity {
    //An adapter, listview and intent are primary classes for data displaying and data transfering
    private CustomAssetAdapter adapter;
    private ListView listView;
    private Intent newActivity;

    private static final int REQUEST_CAMERA = 200;//variable use for permission checking
    //Variables Constant to be use for recognizing data across intent data passing-sending and receiving data
    private static final String ARRAY_LIST = "com.scribblesinc.tams";
    private static final String REC_AUDIO = "com.scribblesinc.tams";
    private static final String ASSET_TITLE = "com.scribblesinc.tams";
    private static final String ASSET_NOTES = "com.scribblesinc.tams";
    private static final String ASSET_CATEGORY = "com.scribblesinc.tams";
    private static final String ASSET_TYPE = "com.scribblesinc.tams";
    public static final String ASSET_LOCATION = "com.scribblesinc.tams";

    //list of variables to be maintating or updating memo and image the rest need to be access via the class asset
    private Bitmap assetMedia_image = null;
    private String assetMedia_voice = null;

    //Stores category id chosen by user, which type uses to create its listview
    private long CatID = 0;

    //boolean if fields has been modified
    private boolean isDescriptionModified = false;
    private boolean isImageModified = false;
    private boolean isAudioModified = false;
    private boolean isTitleModified = false;

    private boolean isAssetExist = false;//if user is looking at an existing asset

    /*
    * Update:
    * onCreate - put the current asset info in there. (take info out of asset class)
    * when you have the categ/type list open, and the user taps +
    *     replace the value in these variables with the new value that the user typed
    * Change onAssetUpdate/onAssetCreate to pull from these variables
    * */
    private String assetCategoryDescription = "";
    private String convertedBitmap;
    private Bitmap scaledMedia;

    //list of items that adapter will use to populate listview
    ArrayList<Item> items = new ArrayList<>();
    ArrayList<LatLng> asset_locations = null;

    //Class for storing data inserted by user
    private Assets asset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* run parents method by extending the existing class or run this
        *  class in addition to parent's class*/
        asset_locations = new ArrayList<>();
        super.onCreate(savedInstanceState);
        // set layout for thiw window activity
        setContentView(R.layout.activity_asset_add);
        // Instantiating the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // pass context and data to the custom adapter
        adapter = new CustomAssetAdapter(this, generateData());
        // get listview to be use for this activity
        listView = (ListView) findViewById(R.id.listView_aa);
        //this restores the stuff in case phone is rotated.
        if (savedInstanceState != null) {
            onRotate(savedInstanceState);
        }
        if(assetMedia_image != null){
            adapter.setBitMap(assetMedia_image);
        }
        // Assign adapter to listview
        listView.setAdapter(adapter);

        //Check if activity is called via intent
        if (getCallingActivity() != null) {
            isAssetExist = true;
            //update activity with information sent
            updateList();
        }

        //listiner for listview so whenever user clicks on any row in listview it will create an event
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
                                                        //initialize intent to be called for name
                                                        newActivity = new Intent(AssetAdd.this, TitleofAsset.class);
                                                        //put title item on intent so that TitleofAsset can have it
                                                        newActivity.putExtra(ASSET_TITLE, adapter.getItem(1).getDescription());
                                                        //start activity ezpecting a result from it
                                                        startActivityForResult(newActivity, 0);//upon return go t given requestCode of 0
                                                        break;
                                                    case 2://category
                                                        listCategory();//call function to create activity listview for category
                                                        break;
                                                    case 3://type
                                                        if(CatID != 0) {
                                                            listType(CatID);
                                                        }else{
                                                            Toast.makeText(getApplicationContext(), "Use Category First", Toast.LENGTH_LONG).show();
                                                        }
                                                        break;
                                                    case 4://location
                                                        newActivity = new Intent(AssetAdd.this, SelectLocation.class);
                                                        newActivity.putParcelableArrayListExtra(ASSET_LOCATION, asset_locations);
                                                        startActivityForResult(newActivity,4); //on return requestcode is 4
                                                        break;
                                                    case 5://voice memo
                                                        newActivity = new Intent(AssetAdd.this, AudioCapture.class);
                                                        //send assetMedia_voice to allow user to play or record a new audio
                                                        newActivity.putExtra(REC_AUDIO, assetMedia_voice);
                                                        startActivityForResult(newActivity, 5);//request code 5
                                                        break;
                                                    case 6://description
                                                        newActivity = new Intent(AssetAdd.this, NotesCapture.class);
                                                        newActivity.putExtra(ASSET_NOTES, adapter.getItem(6).getDescription());
                                                        startActivityForResult(newActivity, 6);//request code 6
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

    /* The list of items generated to be added to ListView as default*/
    private ArrayList<Item> generateData() {

        // Data populated onCreate
        items.add(new Item("Image", "Image of Asset", R.drawable.ic_camera_alt));
        items.add(new Item("Name", null));
        items.add(new Item("AssetCategory", null));
        items.add(new Item("Type", null));
        items.add(new Item("Asset Location", "Asset location"));
        items.add(new Item("Voice Memo", "Record Voice Memo", R.drawable.ic_mic));
        items.add(new Item("Description", null));

        return items;
    }

    //Method to populate listview based on data sent by AssetList
    private void updateList() {

        asset = (Assets) getIntent().getParcelableExtra(ARRAY_LIST);
        Map<String, AssetLocation> asset_locs;// = new HashMap<String, AssetLocation>();
        if (asset != null) {
            //Store data
            assetMedia_image = null;//image to be handled for displaying
            assetMedia_voice = asset.getMedia_voice_url();

            //setting activity's adapter for listview new data to display
            adapter.getItem(1).setDescription(asset.getName());
            adapter.getItem(2).setDescription(asset.getCategory());
            adapter.getItem(3).setDescription(asset.getAsset_type());
            asset_locs = asset.getLocations();

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
                asset_locations = new ArrayList<>();
                adapter.getItem(4).setDescription("0 selected");
            }

            adapter.getItem(6).setDescription(asset.getDescription());
            //store the category id sent by listview so category and type can display
            CatID = Long.parseLong(asset.getCategory_id());


            //update listview with new values.
            listView.setAdapter(adapter);
        }
    }

    /* Start activityForResult() will call the intent to my photo, notes and audio, whatever is
    *  sent back is received here onActivityResult(), thus here we process result to be outputted on
    *  addAdd listview*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //based on the requestCode we created, the appropriate we go to the appropiate case
        switch (requestCode) {
            case 0://Name
                //Check that data exist before trying to assign it to something
                if (data != null) {
                    //check if description has been modified
                    if(adapter.getItem(1).getDescription() != null) {//if description is not null
                        if (adapter.getItem(1).getDescription().compareTo(data.getStringExtra(ASSET_TITLE)) != 0) {//data has been modified
                            this.isTitleModified = true;
                        }
                    }

                    //gets the title sent from  TitleofAsset activity
                    adapter.getItem(1).setDescription(data.getStringExtra(ASSET_TITLE));
                    //display new changes
                    listView.setAdapter(adapter);
                }
                break;

            case 1://Image Note: requestCode for image is 1
                //Check that data exist before trying to assign it to something
                if(data != null) {
                    //Image uses bundle instead of intent to receive data sent back from camera
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
/*
                    if(height>width) {
                        scaledMedia = createScaledBitmap(fullrezMedia, 800, 550, false);
                    }else{
                        scaledMedia = createScaledBitmap(fullrezMedia, 550, 800, false);
                    }
*/
                    //Display new changes
                    listView.setAdapter(adapter);
                }

                break;
            case 2://comes from category row view
                //check that data exist
                if(data != null){
                    //User selected from Listview
                    if(RESULT_OK == resultCode){

                    //set corresponding row in listview with user category selection
                    adapter.getItem(2).setDescription(((AssetCategory) data.getParcelableExtra(ASSET_CATEGORY)).getName());
                    //update listview
                    listView.setAdapter(adapter);
                    //set global id
                    CatID = ((AssetCategory) data.getParcelableExtra(ASSET_CATEGORY)).getId();

                    }else{//User wants to create a new Category
                        //read new category name and description and have it update/create new asset
                    }

                }
                break;


            case 3://comes from type row view
                if(data != null){
                    //set corresponding row in listview with user type selection
                    adapter.getItem(3).setDescription(((AssetType)data.getParcelableExtra(ASSET_TYPE)).getName());
                    //update listview
                    listView.setAdapter(adapter);
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
                }
                break;
            default://nothing
                Toast.makeText(getApplicationContext(), "Row view not on listview", Toast.LENGTH_LONG).show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //What buttons to show on menu
        if (!isAssetExist) {//if asset doesnt exist, show this menu
            getMenuInflater().inflate(R.menu.menu_add_asset, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_asset_modify, menu);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //When user presses buttons on menu
        int id = item.getItemId();//get id of item pressed
        // if the back button is pressed
        if (id == android.R.id.home) {
            finish(); //goes back to the previous activity
        }
        // when the create button is pressed
        if (id == R.id.action_create) {
            assetToCreate();
        }
        //reset activity
        if (id == R.id.action_reset) {

            Intent intent = new Intent(this, AssetAdd.class);
            startActivity(intent);
            finish(); // This opens a new AssetAdd and closes the current one.

        }
        //update asset
        if (id == R.id.action_update) {
            assetToUpdate();
        }
        //Delete asset
        if (id == R.id.action_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmationAlertDialogStyle);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
            builder.setMessage("Selecting 'OK' will delete this asset! ");
            builder.setTitle("Attempting to Delete Asset");

            //Get the AlertDialog from create();
            AlertDialog d = builder.create();
            d.show();
        }

        return super.onOptionsItemSelected(item);
    }

    //listCategory method takes no parameters but calls ListCategory activity
    public  void listCategory() {
        //AssetCategory calls list goes and comebacks with an arrayList of category
        AssetCategory.list(new Response.Listener<ArrayList<AssetCategory>>() {
            public void onResponse(final ArrayList<AssetCategory>response){
                if(response !=null){
                    //create an intent, put that arraylist category and sent it to ListCategory
                    //with a waiting responsecode of 2
                    newActivity = new Intent(AssetAdd.this,ListCategory.class);
                    newActivity.putParcelableArrayListExtra(ASSET_CATEGORY,response);
                    startActivityForResult(newActivity,2);

                }
            }
        },null);


    }
    //listType method takes one parameters and calls ListType activity
    public void listType(long id){
        //AssetType calls list with given key goes and comebacks with an arrayList of types
        AssetType.list(id, new Response.Listener<ArrayList<AssetType>>() {
            @Override
            public void onResponse(ArrayList<AssetType> response) {
                if(response != null){
                    //create an intent, put that arraylist type and sent it to ListType
                    //with a waiting responsecode of 3
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
                Toast.makeText(getApplicationContext(), "ASSET DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();
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
    //method to create new asset with adapter's corresponding data
    public void assetToCreate() {
        //Pull info from fields
        String name = adapter.getItem(1).getDescription();
        String category = adapter.getItem(2).getDescription();
        String categoryDescription = assetCategoryDescription;
        String typeName = adapter.getItem(3).getDescription();
        String description = adapter.getItem(6).getDescription();
        ArrayList<AssetLocation> locations = new ArrayList<>();

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
    //Method to update asset
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
                                Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
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
    //Function called if for updating or loading new image to the backend
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
    //Function called if for updating or loading new memo to the backend
    public void uploadMemoIfNeeded(final Response.Listener<Boolean> listener)
    {
        if (memoWasModified())
        {

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

    //check is data has been modified
    public boolean imageWasModified()
    {
        if(!isImageModified) return false;
        else return false;
    }
    //dismisses activity
    public void dismissView()
    {
        this.finish();
    }
}