package com.scribblesinc.tams.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.Assets;

import java.util.List;

/**
 * Created by Joel on 10/26/2016.
 *
 * Adapter which provides data to list view for AssetList, using BaseAdapter and
 * not ArrayAdapter
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Assets> assetList;
    //ImageLoader imageLoader

    public CustomListAdapter(Activity activity, List<Assets> assetList) {
        this.activity = activity;
        this.assetList = assetList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)//create one
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_asset_list, parent, false);//will inflate with given parent bu won't attach it to it

        //get text view
        TextView a_title = (TextView) convertView.findViewById(R.id.title_asset);
        TextView a_notes = (TextView) convertView.findViewById(R.id.notes_asset);
        TextView a_location = (TextView) convertView.findViewById(R.id.location_asset);

        //getting asset data for row
        Assets asset = assetList.get(position);

        //set text view
        a_title.setText(asset.getName());
        a_notes.setText(asset.getDescription());
        //return row view
        return convertView;
    }

    @Override
    public int getCount() {
        return assetList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int location) {
        return assetList.get(location);
    }
}