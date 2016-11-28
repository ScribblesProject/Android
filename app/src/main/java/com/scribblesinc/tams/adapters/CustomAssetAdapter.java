package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import android.graphics.Bitmap;


import android.graphics.drawable.BitmapDrawable;

import com.scribblesinc.tams.R;
import com.scribblesinc.tams.androidcustom.Item;
/**
 * Created by Joel on 10/26/2016.
 */

//custom ArrayAdapter
public class CustomAssetAdapter extends ArrayAdapter<Item>{
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private Bitmap ImageBitmap;

    public CustomAssetAdapter(Context context, ArrayList<Item> itemsArrayList){
        super(context, R.layout.content_asset_add,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;

    }
    //Get a View that displays the data at the specified position in the data set
    //called when rendering list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View should be created based on the type returned from getItemViewtype(int pos)
        if (convertView == null) {
            //create inflater
            LayoutInflater myinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //get rowView from inflater
            convertView = myinflater.inflate(R.layout.content_asset_add, parent, false);

            if(position == 0 && ImageBitmap != null){                convertView.setBackground(new BitmapDrawable(getContext().getResources(), ImageBitmap));

                System.out.println("made it to getView test");
            }

            //Get the two text view from the rowView
            // ImageView imageView_c = (ImageView) rowView.findViewById(R.id.mic_camera);
            ImageView imgView = (ImageView) convertView.findViewById(R.id.mic_icon);
            //ImageView imgTestView = (ImageView) convertView.findViewById(R.id.background_test);
            TextView valueView = (TextView) convertView.findViewById(R.id.value);
            TextView labelView = (TextView) convertView.findViewById(R.id.label);

            //set the text from textView
            //imgView.setImageResource(itemsArrayList.get(position).getIcon());
            imgView.setImageResource(itemsArrayList.get(position).getIcon());
            //imgTestView.setImageResource(itemsArrayList.get(position).getIcon());
            valueView.setText(itemsArrayList.get(position).getDescription());
            labelView.setText(itemsArrayList.get(position).getTitle());
        }
        //return rowView
        return convertView;
    }

    public int getCount(){
        return itemsArrayList.size();
    }
    public long getItemId(int position){
        return position;
    }
    /*These handle the case where you want different types of view for different rows*/

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

    public void setBitMap(Bitmap imageBitmap){
        ImageBitmap = imageBitmap;
    }

}