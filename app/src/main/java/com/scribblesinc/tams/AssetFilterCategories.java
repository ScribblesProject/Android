package com.scribblesinc.tams;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;;
import com.android.volley.Response;
import com.scribblesinc.tams.adapters.CustomAssetFilterAdapter;
import com.scribblesinc.tams.backendapi.AssetCategory;
import java.util.ArrayList;

/*
 * This is the "Asset Category" view. It display the types of categories available to the user.
 *
 * fetchAssetCategoryFilter() first gets the type of categories from the server via the response method in AssetCategory (located in backend api)
 * It then simply sends this response to the myFilterAdapter for which handles the display. After that the adapter listens to see what row gets tapped
 * and sends that information, category selected/category id, via intent back to Asset Filter, which then ungrays out Asset Type
 */

public class AssetFilterCategories extends AppCompatActivity {
    private static final String ASSET_FILTER_CATEGORY = "ASSET_FILTER_CATEGORY"; //send the data to the previous activity
    private String[] categoryFilter = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_filter_categories);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asset_filter_categories, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAssetCategoryFilter();
    }

    private void fetchAssetCategoryFilter() {
        AssetCategory.list(new Response.Listener<ArrayList<AssetCategory>>() {
            @Override
            public void onResponse(final ArrayList<AssetCategory> response) { //this is where we get the AssetCategory Responses
                if (response != null) {
                    final ListView assetFilterListView = (ListView) findViewById(R.id.content_filter_categories_list_view);
                    CustomAssetFilterAdapter myFilterAdapter = new CustomAssetFilterAdapter(AssetFilterCategories.this, response, R.layout.content_asset_filter_categories, R.id.content_filter_categories_text_view_1, R.id.content_filter_categories_text_view_2);
                    assetFilterListView.setAdapter(myFilterAdapter);
                    assetFilterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int row, long l) { //once a user clicks that category will be picked
                            TextView getFilterCategory = (TextView) view.findViewById(R.id.content_filter_categories_text_view_1);
                            String filterCategory = getFilterCategory.getText().toString(); //store the category
                            String categoryIDString = Long.toString(response.get(row).getId());
                            Intent sendToPreviousActivity = new Intent();
                            categoryFilter[0] = filterCategory;
                            categoryFilter[1] = categoryIDString;
                            sendToPreviousActivity.putExtra(ASSET_FILTER_CATEGORY, categoryFilter);  //send it back to the previously called activity
                            setResult(RESULT_OK, sendToPreviousActivity);
                            finish();
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