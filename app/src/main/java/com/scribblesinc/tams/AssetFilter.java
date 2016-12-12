package com.scribblesinc.tams;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.scribblesinc.tams.adapters.CustomAssetFilterAdapter;


/*
 * This is the "Asset Filter" view. It handles sending the user to Asset Category and Asset Type via intent. It also handles displays the selected
 * category and type a user has picked. Lastly, a user is able to clear the selected filter or apply it. Applying the filter sends a user back to
 * the "Asset Map" which then displays which assets meet the applied filter. These are both handled in
 *
 * onOptionsItemSelected(MenuItem item) handles both "Clear" and Apply. Notice that clicking Apply brings up an Alert dialog so that a user can
 * make the choice to apply the filter or not.
 *
 * onActivityResult(int requestCode, int resultCode, Intent data) handles all the intent data being sent back
 */

public class AssetFilter extends AppCompatActivity { //Filter Map ist stored here
    private static final String ASSET_FILTER = "ASSET_FILTER";
    private static final String ASSET_FILTER_CATEGORY = "ASSET_FILTER_CATEGORY"; //Used as a key for AssetFilter Types to see what category was selected
    private static final String ASSET_FILTER_TYPE = "ASSET_FILTER_TYPE"; //Used as a key to retrieve the type selected
    private String categorySelected = ""; //stores the category selected by the user
    private String categoryID = "";
    private String typeSelected = ""; //stores the type selected by the user
    private String[] categoryTypeFilter = {categorySelected, typeSelected};
    private String[] typeFilter = {categorySelected, categoryID};
    private ListView listView;
    private CustomAssetFilterAdapter myFilterAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//on create is what happens as soon as a user goes to a new activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView = (ListView) findViewById(R.id.content_filter_list_view);
        myFilterAdapter = new CustomAssetFilterAdapter(AssetFilter.this, categorySelected, R.layout.content_asset_filter, R.id.content_filter_text_view_1, R.id.content_filter_text_view_2);
        listView.setAdapter(myFilterAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0: //when Asset Category is tapped
                        intent = new Intent(AssetFilter.this, AssetFilterCategories.class);
                        myFilterAdapter.setTypeSelected("");
                        startActivityForResult(intent, position); //start new activity and send an intent to retrieve the data
                        break;
                    case 1: //when Asset Type is tapped
                        intent = new Intent(AssetFilter.this, AssetFilterTypes.class);
                        typeFilter[0] = categorySelected;
                        typeFilter[1] = categoryID;
                        intent.putExtra("ASSET_FILTER_CATEGORY", typeFilter); //give the new activity a key to get the goodies
                        startActivityForResult(intent, position);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asset_filter, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_clear){  //when the edit button is pressed
            categorySelected = "";
            myFilterAdapter.setCategorySelected(categorySelected);
            listView.setAdapter(myFilterAdapter);
        }

        if(id == R.id.action_apply_filter){ //if the add asset button is pressed
            if(categorySelected == null || categorySelected.isEmpty() || typeSelected == null || typeSelected.isEmpty()) { //if either category or type is null display message
                Toast.makeText(this, R.string.check_category_type_selection , Toast.LENGTH_LONG).show();
            }else { //display dialog to ask a user if they would like to apply the filter to the map
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.ConfirmationAlertDialogStyle);
                builder.setTitle(R.string.apply_filter_title);
                builder.setMessage(R.string.apply_filter_message);
                builder.setNegativeButton(android.R.string.cancel, null); //cancel
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { //if ok
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { //on press send the category and filter in an array to the main activity
                        Intent sendToPreviousActivity = new Intent();
                        categoryTypeFilter[0] = categorySelected;
                        categoryTypeFilter[1] = typeSelected;
                        sendToPreviousActivity.putExtra(ASSET_FILTER, categoryTypeFilter);
                        setResult(RESULT_OK, sendToPreviousActivity);
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        if(id == android.R.id.home){
            finish(); //goes back to the previous activity
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null) {
            return;
        }
        switch(requestCode) {
            case 0: //category selected
                String[] categoryFilter = data.getStringArrayExtra(ASSET_FILTER_CATEGORY);
                categorySelected = categoryFilter[0]; //saves selected category from Asset Filter Categories
                categoryID = categoryFilter[1];
                myFilterAdapter.setCategorySelected(categorySelected);
                listView.setAdapter(myFilterAdapter); //display it
                break;
            case 1: //filter selected
                typeSelected = data.getStringExtra(ASSET_FILTER_TYPE); //saves selecte category from Asset Filter Types
                myFilterAdapter.setTypeSelected(typeSelected);
                listView.setAdapter(myFilterAdapter); //display it
                break;
            default:
                System.out.println("\nError request code not found\n");
                break;
        }
    }
}