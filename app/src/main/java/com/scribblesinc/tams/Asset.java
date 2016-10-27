
package com.scribblesinc.tams;
import java.util.ArrayList;

/**
 * Created by Alejandro on 10/24/2016.
 * Class used for Object creation of Assets from JSON
 * Uses GSON to convert JSON to Java objects for manipulation
 */

public class Asset {
    private long id;
    private String name;
    private String description;
    private String category;
    private String categoryId;
    private String categoryDescription;
    private String assetType;
    private String mediaImageURL;
    private String mediaVoiceURL;
    private ArrayList<AssetCoordinates> locations;

    public Asset(long ai, String n, String des, String cat, String catId, String catDes, String at, String mIU, String mVU, ArrayList<AssetCoordinates>locs){
        id = ai;
        name = n;
        description = des;
        category = cat;
        categoryId = catId;
        categoryDescription = catDes;
        assetType = at;
        mediaImageURL = mIU;
        mediaVoiceURL = mVU;
        locations = locs;
    }

    public long getId(){
        return id;
    }

    public void setId(long ai){
        id = ai;
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        name = n;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String des){
        description = des;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String cat){
        category = cat;
    }

    public String getCategoryId(){
        return categoryId;
    }

    public void setCategoryId(String catId){
        categoryId = catId;
    }

    public String getCategoryDescription(){
        return categoryDescription;
    }

    public void setCategoryDescription(String catDes){
        categoryDescription = catDes;
    }

    public String getAssetType(){
        return assetType;
    }

    public void setAssetType(String at){
        assetType = at;
    }

    public String getMediaImageURL(){
        return mediaImageURL;
    }

    public void setMediaImageURL(String mIU){
        mediaImageURL = mIU;
    }

    public String getMediaVoiceURL(){
        return mediaVoiceURL;
    }

    public void setMediaVoiceURL(String mVU){
        mediaVoiceURL = mVU;
    }

    public ArrayList<AssetCoordinates> getLocations(){
        return locations;
    }

    public void setLocations(ArrayList<AssetCoordinates> locs){
        locations = locs;
    }
}
