package com.scribblesinc.tams.androidcustom;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Joel on 10/19/2016.
 *
 * Purpose: we need to create a java class with field that will be represented on AssetList
 *          The field names must match with names in JSON
 */

public class AssetItems {
    private String name;
    private String description;
    private String category;
    @SerializedName("category-id")
    private String categoryId;
    @SerializedName("category-description")
    private String categoryDescription;
    @SerializedName("asset-type")
    private String assetType;
    @SerializedName("media-image-url")
    private String mediaImageUrl;
    @SerializedName("media-voice-url")
    private String mediaVoiceUrl;
    private Locations locations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getMediaImageUrl() {
        return mediaImageUrl;
    }

    public void setMediaImageUrl(String mediaImageUrl) {
        this.mediaImageUrl = mediaImageUrl;
    }

    public String getMediaVoiceUrl() {
        return mediaVoiceUrl;
    }

    public void setMediaVoiceUrl(String mediaVoiceUrl) {
        this.mediaVoiceUrl = mediaVoiceUrl;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }
    @Override
    public String toString(){
        return name+"-"+description+"-"+category+"-"+categoryDescription+"-"+assetType;
    }
}

