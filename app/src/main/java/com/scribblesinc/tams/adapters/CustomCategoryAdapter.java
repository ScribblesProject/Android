package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.AssetCategory;

import java.util.ArrayList;

/**
 * Created by Joel on 11/30/2016.
 * INFORMATION: This is a simple ArrayAdapter that is use to display the ListView for
 * ListCategory class. This arrayAdapter uses an AssetCategory arraylist of Items to create the list shown on  ListCategory's
 * ListView.
 */

public class CustomCategoryAdapter extends ArrayAdapter<AssetCategory> {
    private final Context context;
    private final ArrayList<AssetCategory> assetcategorylist;


    public CustomCategoryAdapter(Context context, ArrayList<AssetCategory> assetCategories){
        super(context, R.layout.content_asset_add, assetCategories);
        this.context = context;
        this.assetcategorylist = assetCategories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //if view doesnt exist
        if(convertView == null){
            LayoutInflater myinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //get rowView from inflater
            convertView = myinflater.inflate(R.layout.content_listcategory,parent,false);
        }

        //get
        TextView titleView = (TextView) convertView.findViewById(R.id.category_title);
        TextView titleDescription = (TextView) convertView.findViewById(R.id.category_description);

        //set
        titleView.setText(assetcategorylist.get(position).getName());
        titleDescription.setText(assetcategorylist.get(position).getDescription());

        return convertView;
    }
    //return size of ArrayList
    public int getCount(){
        return assetcategorylist.size();
    }
    //return position in Arraylist
    public long getItemId(int position){
        return position;
    }



}
