package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.scribblesinc.tams.AssetAdd;
import com.scribblesinc.tams.R;
import com.scribblesinc.tams.androidcustom.Item;
/**
 * Created by Joel on 10/26/2016.
 */
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.res.ResourcesCompat;
public class CustomAssetAdapter extends ArrayAdapter<Item>{
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private boolean img;

    public CustomAssetAdapter(Context context, ArrayList<Item> itemsArrayList){
        super(context, R.layout.content_asset_add,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }
    //Get a View that displays the data at the specified position in the data set
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View  view = super.getView(position, convertView, parent);
        //if(position == 1){

         //   view.setBackgroundResource(R.drawable.green_btn);

        //}
        //else
        //{
            //view.setBackgroundResource();
        //}//
        //View should be created based on the type returned from getItemViewtype(int pos)
        if (convertView == null) {
            //create inflater
            LayoutInflater myinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //get rowView from inflater
            convertView = myinflater.inflate(R.layout.content_asset_add, parent, false);

            //Get the two text view from the rowView
            // ImageView imageView_c = (ImageView) rowView.findViewById(R.id.mic_camera);
            ImageView imgView = (ImageView) convertView.findViewById(R.id.mic_icon);
            TextView valueView = (TextView) convertView.findViewById(R.id.value);
            TextView labelView = (TextView) convertView.findViewById(R.id.label);

            //set the text from textView
            imgView.setImageResource(itemsArrayList.get(position).getIcon());
            //imgView.setImageResource(itemsArrayList.get(position).getIcon());
            valueView.setText(itemsArrayList.get(position).getDescription());
            labelView.setText(itemsArrayList.get(position).getTitle());

            if(position == 0) {

                //need to now set the drawable to a empty drawable and override it every time

               // convertView.setBackgroundResource(R.drawable.checkmark);
                //convertView.setVisibility(View.INVISIBLE);
                //convertView.setBackground(R.drawable.oval);
                //ImageView imgView2 = (ImageView) convertView.findViewById(R.id.checkmark);
                //imgView.setImageResource(R.id.checkmark);
                //imgView2.setImageResource(R.drawable.oval);


            }
        }
        //return rowView
        return convertView;
    }

    //Returns the number of types of Views that will be created by getView(int,View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        //Returns the # of types of Views that wil be created by this adapter each type
        //represents a set of views that can be converted
        return 3;
    }

    //Get the type of view that will be created by getView for the specified item
    @Override
    public int getItemViewType(int position) {
        //Return an integer here representing the type of View, due note that integer must be in the
        //range 9 to getViewTypeCount()-1
        return 1;//Item.ColorValues.values().length;
    }


}
