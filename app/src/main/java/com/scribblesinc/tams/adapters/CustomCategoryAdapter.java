package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.AssetCategory;

import java.util.ArrayList;

/**
 * Created by Joel on 11/30/2016.
 */

public class CustomCategoryAdapter extends ArrayAdapter<AssetCategory> {
    public CustomCategoryAdapter(Context context, ArrayList<AssetCategory> assetCategories){
        super(context, R.layout.content_asset_add, assetCategories);
    }

}
