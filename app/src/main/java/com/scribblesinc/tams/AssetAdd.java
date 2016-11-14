package com.scribblesinc.tams;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ListView;
import android.widget.Toast;

import com.scribblesinc.tams.adapters.CustomAssetAdapter;
import com.scribblesinc.tams.androidcustom.Item;
import android.graphics.BitmapFactory;
import java.util.ArrayList;
import android.widget.ImageView;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import java.io.IOException;
import android.graphics.Bitmap;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.support.v4.content.FileProvider;
import android.util.Log;
public class AssetAdd extends AppCompatActivity {//AppCompatActivity

    private CustomAssetAdapter adapter;
    private ListView listView;
    private String name = "";
    private String description = "";
    private String contextValue = "";
    private String typeValue = "";
    private boolean isType;
    private Intent newActivity;
    private static final int REQUEST_CAMERA = 200;

    private static String root = null;
    private static String imageFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";

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
        adapter = new CustomAssetAdapter(this, generateData());
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
                        startActivityForResult(newActivity,1);
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
                //Toast.makeText(getApplicationContext(), "App has microphone capturing permission", Toast.LENGTH_LONG).show();
                newActivity = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (newActivity.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.example.android.fileprovider",
                                photoFile);
                        newActivity.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(newActivity,0);
                        newActivity.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName));
                    }
                }


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
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }



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
        int id = item.getItemId();
        if(isType) {
            //Toast.makeText(getApplicationContext(),"isType",Toast.LENGTH_LONG).show();


            if (id == R.id.caution_sign){
                typeValue = "caution sign";

            }
            if (id == R.id.traffic_sign) {
                typeValue = "traffic sign";

            }
            adapter.getItem(3).setDescription(typeValue);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();

        }else{
            //Toast.makeText(getApplicationContext(),"is category",Toast.LENGTH_LONG).show();
            //probably want another one here as well.

            if (id == R.id.stop_light) {
                contextValue = "stop light";

            }
            if (id == R.id.road_sign) {
                contextValue = "road sign";

            }

            adapter.getItem(2).setDescription(contextValue);
            listView.setAdapter(adapter);
            this.registerForContextMenu(listView);
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    // The array list of items to be added to my ListView
    private ArrayList<Item>generateData() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("Image","Image of Asset",R.drawable.ic_camera_alt));
        items.add(new Item("Name", name));
        items.add(new Item("AssetCategory",contextValue));
        items.add(new Item("Type",typeValue));
        items.add(new Item("AssetLocation","Asset location"));
        items.add(new Item("Voice Memo","Record Voice Memo",R.drawable.ic_mic));
        items.add(new Item("Description", description));
        items.add(new Item(" "," "));//empty item to permit scrolling
        return items;
    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    /* Start activityForResult() will call the intent to my photo, notes and audio, whatever is
    *  sent back is receive here onActivityResult(), thus here we process result to be outputted on
    *  addAdd viewlist*/
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // result of each view in list will come here and here it will be handled
        // accordingly
        // Note: Case statement numbers cause conflicts. In "Name" typing something in and pressing done gives us REQ = 1, RES = -1, and
        // ID = {(has extras)}, while pressing back (on both app and phones gives) 1, 0, and null respectively
        switch (requestCode) {
            case 0:

                //root = Environment.getExternalStorageDirectory().toString()
                  //      + "/Your_Folder";

                // Creating folders for Image
               // imageFolderPath = root + "/saved_images";
               // File imagesFolder = new File(imageFolderPath);
                //imagesFolder.mkdirs();
               // imageName = "test.png";

                // Creating image here

                //File image = new File(imageFolderPath, imageName);

                //fileUri = Uri.fromFile(image);
                //Toast.makeText(getApplicationContext(), "Picture to be handled", Toast.LENGTH_LONG).show();
                //data.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                //Bitmap bitmap = null;
                //try {
                 //   GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
                 //   bitmap = getImageThumbnail.getThumbnail(fileUri, this);
              //  } catch (FileNotFoundException e1) {
                ///    // TODO Auto-generated catch block
               //   e1.printStackTrace();
              //  } catch (IOException e1) {
                    // TODO Auto-generated catch block
                //    e1.printStackTrace();
              // }

                // Setting image image icon on the imageview
                //Uri takenPhotoUri = getPhotoFileUri(photoFileName);
                // by this point we have the camera photo on disk
                //Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());
               // ImageView imageView = (ImageView) this
                    //   .findViewById(R.id.checkmark);

                //imageView.setImageBitmap(takenImage);
                //ImageView mImageView = (ImageView) this.findViewById(R.id.checkmark);
               // Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //mImageView.setImageBitmap(imageBitmap);
                galleryAddPic();
                break;
            case 1:
                System.out.println("RQC: " + requestCode + " RC: " + resultCode);

                if (data != null) { // data can be null if back button is pressed!!!
                     //gets the title from the key that was passed by the activity in TitleofAsset
                    name = data.getStringExtra("assetTitle");
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(1).setDescription(name);
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
                    description = data.getStringExtra("assetNotes");
                    //gets the item at index 1 (the description of the title) and changes it
                    adapter.getItem(6).setDescription(description);
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
        if(id == android.R.id.home) {
            finish(); //goes back to the previous activity
        }
        // when the filter button is pressed
        if(id == R.id.action_done) {
            Toast.makeText(getApplicationContext(), "Not working ", Toast.LENGTH_SHORT).show();
        }
        // resets the asset being created
        if (id == R.id.action_reset) {
            if(!(name.isEmpty() && description.isEmpty())) {
                name = "";
                description = "";
                contextValue = "";
                typeValue = "";

                adapter.getItem(1).setDescription(name);
                //gets the item at index 1 (the description of the title) and changes it
                adapter.getItem(2).setDescription(contextValue);

                adapter.getItem(3).setDescription(typeValue);

                adapter.getItem(6).setDescription(description);
                //setListAdapter aka assign adapter to listview
                listView.setAdapter(adapter);
                //creating a contextmeny for listview
                this.registerForContextMenu(listView);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
