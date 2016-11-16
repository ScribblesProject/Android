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
import com.google.android.gms.vision.text.Text;
import com.scribblesinc.tams.R;
import com.scribblesinc.tams.Assets;
import com.scribblesinc.tams.patterns.AppController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Joel on 10/26/2016.
 *
 * Adapter which provides data to list view for AssetList, using BaseAdapter and
 * not ArrayAdapter
 */
public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList itemsAsset;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Map<String,ArrayList<Assets>> itemAssetMap) {
        itemsAsset = new ArrayList();//create an arraylist
        itemsAsset.addAll(itemAssetMap.entrySet());//get collection
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_asset_list, parent, false);//will inflate with given parent bu won't attach it to it

        //image
        if(imageLoader==null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imgAsset= (NetworkImageView) convertView.findViewById(R.id.img_asset);
        //get text view
        TextView assetTitle = (TextView) convertView.findViewById(R.id.title_asset);
        TextView assetNotes = (TextView) convertView.findViewById(R.id.notes_asset);
        //TextView alocation = (TextView) convertView.findViewById(R.id.location_asset);

        //getting asset data for row
        //Assets asset = ItemsAssetMap.get(1).get(position);
        Map.Entry<String, ArrayList<Assets>> item =  this.getItem(position);

        //TAG, String.valueOf(assets.get(0).sortedLocations()));
        //set text view
        //imgAsset.setImageUrl(item.get(4).getMedia_image_url(), imageLoader);
      //  imgAsset.setImageUrl(getKey);
        //assetTitle.setText(item.getName());
        //assetNotes.setText(item.getDescription());
        //return row view
        return convertView;
    }
    @Override
    public Map.Entry<String, ArrayList<Assets>> getItem(int position){
        return (Map.Entry) itemsAsset.get(position);
    }

    @Override
    public int getCount() {
        return itemsAsset.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}