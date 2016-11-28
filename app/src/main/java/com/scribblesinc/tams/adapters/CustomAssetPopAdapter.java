package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.scribblesinc.tams.R;

import com.scribblesinc.tams.backendapi.Assets;

import java.util.ArrayList;


public class CustomAssetPopAdapter extends ArrayAdapter<Assets> {
    private final Context context;
    private final ArrayList<Assets> assets;

    public CustomAssetPopAdapter(Context context, ArrayList<Assets> assets) {
        super(context, R.layout.content_asset_add, assets);
        this.context = context;
        this.assets = assets;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //return views
        return convertView;
    }
}//end oc CustomAssetPopAdapter class
