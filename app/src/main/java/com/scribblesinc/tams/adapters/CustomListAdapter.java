package com.scribblesinc.tams.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.Assets;
import com.scribblesinc.tams.network.AppRequestManager;

import java.util.List;

/**
 * Created by Joel on 10/26/2016.
 *
 * Adapter which provides data to list view for AssetList, using BaseAdapter and
 * not ArrayAdapter
 */
public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Activity activity;
    private List<Assets> assetList;
    ImageLoader imageloader = AppRequestManager.getInstance().getImageLoader();
    //ImageLoader imageLoader

    public CustomListAdapter(Activity activity, List<Assets> assetList) {
        this.assetList = assetList;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //we inflate the xml which gives us a view
        if(inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_asset_list, parent, false);//will inflate with given parent bu won't attach it to it
        //if imageloader is not initialize
        if(imageloader==null)
            imageloader = AppRequestManager.getInstance().getImageLoader();

        //Get the widget with id name which is defined in the xml of the row
        NetworkImageView imgAsset = (NetworkImageView) convertView.findViewById(R.id.img_asset);
        TextView a_title = (TextView) convertView.findViewById(R.id.title_asset);
        TextView a_notes = (TextView) convertView.findViewById(R.id.notes_asset);
        //TextView a_location = (TextView) convertView.findViewById(R.id.location_asset);

        //Get the item in the adapter
        Assets asset = assetList.get(position);

        //populate the row's with info from the list
        imgAsset.setImageUrl(asset.getMedia_image_url(),imageloader);
        a_title.setText(asset.getName());
        a_notes.setText(asset.getDescription());
        //return generated view
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