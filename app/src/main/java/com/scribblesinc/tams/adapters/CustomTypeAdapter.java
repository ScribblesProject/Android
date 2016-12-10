package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.AssetType;

import java.util.ArrayList;





/**
 * Created by skenn on 12/7/2016.
 *
 * INFORMATION: This is a simple ArrayAdapter that is use to display the ListView for
 * ListType class. This arrayAdapter uses an AssetType arraylist of Items to create the list shown on  ListType's
 * ListView.
 */

public class CustomTypeAdapter  extends ArrayAdapter<AssetType>{
    private final Context context;
    private final ArrayList<AssetType> assetTypeList;


    public CustomTypeAdapter(Context context, ArrayList<AssetType> assetTypes){
        super(context, R.layout.content_listtype, assetTypes);
        this.context = context;
        this.assetTypeList = assetTypes;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //if view doesnt exist
        if(convertView == null){
            LayoutInflater myinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //get rowView from inflater
            convertView = myinflater.inflate(R.layout.content_listtype,parent,false);
        }

        //get
        TextView titleView = (TextView) convertView.findViewById(R.id.Type_Name);
        TextView titleDescription = (TextView) convertView.findViewById(R.id.Type_Notes);

        //set
        titleView.setText(assetTypeList.get(position).getName());
        titleDescription.setText(assetTypeList.get(position).getCategory_name());

        return convertView;
    }
    public int getCount(){
        return assetTypeList.size();
    }
    public long getItemId(int position){
        return position;
    }









}
