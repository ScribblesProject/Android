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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.scribblesinc.tams.R;
import com.scribblesinc.tams.network.AppRequestManager;
import com.scribblesinc.tams.util.Item;


/**
 * Created by Joel on 10/26/2016.
 *
 * INFORMATION: This is a simple ArrayAdapter that is use to display the ListView for
 * AssetAdd class. This arrayAdapter uses an Arraylist of Items to create the list shown on  AssetAdd
 * ListView.
 *
 */
public class CustomAssetAdapter extends ArrayAdapter<Item>{
    private final Context context;
    private final ArrayList<Item> itemsArrayList;
    private Bitmap ImageBitmap;
    private String URL;
    ImageLoader imageloader = AppRequestManager.getInstance().getImageLoader();

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
        }

        if(imageloader==null)
            imageloader = AppRequestManager.getInstance().getImageLoader();

        if(position == 0){

            if(ImageBitmap != null) {
                convertView.setBackground(new BitmapDrawable(getContext().getResources(), ImageBitmap));
            }else {
                if (URL != null) {
                    NetworkImageView imgAsset = (NetworkImageView) convertView.findViewById(R.id.img_asset);
                    imgAsset.setImageUrl(URL, imageloader);
                }

            }
        }




        //Get the widget with id name which is defined in the xml of the row
        ImageView imgView = (ImageView) convertView.findViewById(R.id.mic_icon);
        TextView valueView = (TextView) convertView.findViewById(R.id.value);
        TextView labelView = (TextView) convertView.findViewById(R.id.label);

        //Set the text from textView
        imgView.setImageResource(itemsArrayList.get(position).getIcon());
        valueView.setText(itemsArrayList.get(position).getDescription());
        labelView.setText(itemsArrayList.get(position).getTitle());

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


    @Override
    public int getViewTypeCount() {
        //Returns the number of types of Views that will be created by getView(int,View, ViewGroup)
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        //return the type of view that will be created by getView for the specified item
        return 1;
    }

    public void setBitMap(Bitmap imageBitmap){
        ImageBitmap = imageBitmap;
    }

    public void setURL(String URL){URL = URL;}
}