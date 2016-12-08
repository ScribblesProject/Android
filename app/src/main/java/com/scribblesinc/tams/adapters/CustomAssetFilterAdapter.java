package com.scribblesinc.tams.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.scribblesinc.tams.R;
import com.scribblesinc.tams.backendapi.AssetCategory;
import com.scribblesinc.tams.backendapi.AssetType;
import java.util.ArrayList;

/**
 * Created by Aaron Williams on 12/2/2016.
 */

public class CustomAssetFilterAdapter extends ArrayAdapter {
    private static final String[] categoryTypeArray = {"Asset Category", "Asset Type"}; //stores the category and type names (Asset Category, Asset Type)
    private ViewHolder viewHolder; //used to get to the View Holder variables.
    private String categorySelected; //stores the category that was selected by the user
    private String typeSelected; //stores the type that was selected by the user
    private String assetFilterRowName; //stores the name of each category/type. It's on the left hand side
    private ArrayList<AssetCategory> assetFilterCategoriesList; //response from server that has the category listing
    private ArrayList<AssetType> assetFilterTypesList; //response from server that has the type listing
    private int rLayoutID; //name of .xml file found in layout folder
    private int rID1; //id that is found in said .xml file
    private int rID2; //id that is found in said .xml file
    private LayoutInflater myInflater = null; //used to inflate the layout (only initialized when it's null to save on resources)

    //Constructor for the filter map
    public CustomAssetFilterAdapter(Context context, String newCategorySelected, int newRLayoutID, int newRID1, int newRID2) {
        super(context, newRLayoutID, categoryTypeArray);
        categorySelected = newCategorySelected;
        rLayoutID = newRLayoutID;
        rID1 = newRID1;
        rID2 = newRID2;
    }

    //Constructor for the filter category
    public CustomAssetFilterAdapter(Context context, ArrayList<AssetCategory> newAFCL, int newRLayoutID, int newRID1, int newRID2) {
        super(context, newRLayoutID, newAFCL);
        assetFilterCategoriesList = newAFCL;
        rLayoutID = newRLayoutID;
        rID1 = newRID1;
        rID2 = newRID2;
    }

    //Constructor for the filter type
    public CustomAssetFilterAdapter(Context context, String newTypeSelected, String newCategorySelected, ArrayList<AssetType> newAFTL, int newRLayoutID, int newRID1) {
        super(context, newRLayoutID, newAFTL);
        typeSelected = newTypeSelected;
        assetFilterTypesList = newAFTL;
        categorySelected = newCategorySelected;
        rLayoutID = newRLayoutID;
        rID1 = newRID1;
    }

    static class ViewHolder { //View Holder is used to implement the View Holder Pattern. It saves resource by not having us call findViewById(int) all the time
        TextView myTextView1; //TextView1 is used for the left hand column of filter map, category, and type
        TextView myTextView2; //TextVuew2 is used for the right hand column of filter map, and category (type doesn't need this)
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(myInflater == null) { //Implements View Holder Pattern, below is used to inflate the the layout from a .xml file
            myInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null) { //Implements View Holder Pattern
            convertView = myInflater.inflate(rLayoutID, parent, false); //convertView reuses other view sources to save on performance
            viewHolder = new ViewHolder();
            viewHolder.myTextView1 = (TextView) convertView.findViewById(rID1); //gets the .xml file and sets up the overall look of the text view 1
            if(rID1 != R.layout.content_asset_filter_types) { //since types doesn't use a second text view, we have to make sure we don't get a null pointer
                viewHolder.myTextView2 = (TextView) convertView.findViewById(rID2);
            }
            convertView.setTag(viewHolder); //This tag means that we can call convertView all the time without having to recall findViewById
        }else{
            viewHolder = (ViewHolder) convertView.getTag(); //gets the stored TextViews
        }

        if (rLayoutID == R.layout.content_asset_filter) { //asset filter layout is made here
            assetFilterRowName = categoryTypeArray[position].toString();

            if(assetFilterRowName.equalsIgnoreCase("Asset Type") && categorySelected.isEmpty()) { //checks to see if an Asset Category has been picked. If not Asset Type will remain grayed out
                viewHolder.myTextView1.setText(assetFilterRowName);
                viewHolder.myTextView1.setTextColor(Color.GRAY);
                viewHolder.myTextView1.setEnabled(false);
            }else {
                viewHolder.myTextView1.setTextColor(Color.BLACK);
                viewHolder.myTextView1.setText(assetFilterRowName);
                viewHolder.myTextView1.setEnabled(true);
            }

            if (position == 0) { ///sets the name of the category and type selected so that it can be shown to the user
                viewHolder.myTextView2.setText(categorySelected);
            } else if(isEnabled(position) == false ) {
                viewHolder.myTextView2.setText("");
            } else {
                viewHolder.myTextView2.setText(typeSelected);
            }
        } else if (rLayoutID == R.layout.content_asset_filter_categories) { //this is were the filter category is built the text is set here
            assetFilterRowName = assetFilterCategoriesList.get(position).getName(); //gets the actual name from the server response
            String assetFilterRowDescription = assetFilterCategoriesList.get(position).getDescription(); //gets the actual description from the server response
            viewHolder.myTextView1.setText(assetFilterRowName);
            viewHolder.myTextView2.setText(assetFilterRowDescription);
        }else if(rLayoutID == R.layout.content_asset_filter_types) { //this is where filter types is made
            if (categorySelected.equalsIgnoreCase(assetFilterTypesList.get(position).getCategory_name())) {
                assetFilterRowName = assetFilterTypesList.get(position).getName();
                viewHolder.myTextView1.setText(assetFilterRowName);
                viewHolder.myTextView1.setVisibility(View.VISIBLE); //since some assets won't be shown, this makes sure the ones that will be are visible
            }else{
                viewHolder.myTextView1.setVisibility(View.GONE); //this makes sure we don't have any blank/empty spaces in the list view
            }
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) { //overrides the enable method so that it keeps Asset Type disabled until a user chose a category
        if(position == 1 && assetFilterRowName.equalsIgnoreCase("Asset Type") && categorySelected.isEmpty()) {
            typeSelected = "";
            return false;
        }
        return true;
    }

    public void setCategorySelected(String category) {categorySelected = category;}

    public void setTypeSelected(String type) {typeSelected = type;}
}