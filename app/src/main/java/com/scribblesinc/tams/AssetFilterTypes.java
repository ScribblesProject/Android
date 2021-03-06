package com.scribblesinc.tams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Response;
import com.scribblesinc.tams.adapters.CustomAssetFilterAdapter;
import com.scribblesinc.tams.backendapi.AssetType;
import java.util.ArrayList;

/*
 * This is the "Asset Type" view. It display the types availabe for the specific category specified by the user
 *
 * fetchAssetTypeFilter() gets the typeFilter which olds the previous selected category along with its id. After that we check to see
 * if the category selected matches the type's category. We then display it with the adapter (just like in Asset Category). The adapter again
 * listens for the user to touch a row, and sends it back to Asset Filter

 */

public class AssetFilterTypes extends AppCompatActivity {

    private static final String ASSET_FILTER_CATEGORY = "ASSET_FILTER_CATEGORY"; //key for the category to be received
    private static final String ASSET_FILTER_TYPE = "ASSET_FILTER_TYPE"; //key for the type to be retrieved
    private String typeSelected = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) { //what happens on start
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_filter_types);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAssetTypeFilter();
    }

    private void fetchAssetTypeFilter() { //the server response for getting the asset types is here
        Intent assetCategorySelected = getIntent(); //get the intent from the last activity
        final String[] typeFilter = assetCategorySelected.getStringArrayExtra(ASSET_FILTER_CATEGORY); //get the category selected that was store int the last activity
        String categorySelectedID = typeFilter[1];
        long id = Long.parseLong(categorySelectedID);
        AssetType.list(id, new Response.Listener<ArrayList<AssetType>>() {
            @Override
            public void onResponse(final ArrayList<AssetType> response) {
                if (response != null) {
                    String categorySelected = typeFilter[0];
                    final ListView assetFilterListView = (ListView) findViewById(R.id.content_filter_types_list_view);
                    CustomAssetFilterAdapter myFilterAdapter = new CustomAssetFilterAdapter(AssetFilterTypes.this, typeSelected, categorySelected, response,  R.layout.content_asset_filter_types, R.id.content_filter_types_text_view_1);
                    assetFilterListView.setAdapter(myFilterAdapter);
                    assetFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //oncce a user clicks on the screen the type is selected (this is done by the view)
                            TextView getFilterType = (TextView) view.findViewById(R.id.content_filter_types_text_view_1);
                            String typeSelected = getFilterType.getText().toString(); //parse the string that's in the first text view
                            Intent sendToPreviousActivity = new Intent();
                            sendToPreviousActivity.putExtra(ASSET_FILTER_TYPE, typeSelected); // send the type to back to the original activity that called it
                            setResult(RESULT_OK, sendToPreviousActivity);
                            finish(); // close the activity
                        }
                    });
                }
            }
        }, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
