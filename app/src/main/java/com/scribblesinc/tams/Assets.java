package com.scribblesinc.tams;


import android.location.Location;
import android.nfc.Tag;
import android.util.Log;

import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

public class Assets {
    private static final String TAG = Assets.class.getSimpleName();
    private long id;
    private String name;
    private String description;
    private String category;
    @SerializedName("category-id")
    private String category_id;
    @SerializedName("category-description")
    private String category_description;
    @SerializedName("asset-type")
    private String asset_type;
    @SerializedName("media-image-url")
    private String media_image_url;
    @SerializedName("media-voice-url")
    private String media_voice_url;
    private Map<String, Location> locations;

    public Assets(long id, String name, String description, String category, String category_id, String category_description, String asset_type, String media_image_url, String media_voice_url, Map<String, Location> locations) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.category_id = category_id;
        this.category_description = category_description;
        this.asset_type = asset_type;
        this.media_image_url = media_image_url;
        this.media_voice_url = media_voice_url;
        this.locations = locations;
    }

    public ArrayList<Location> sortedLocations() {
        ArrayList<Location> result = new ArrayList<>();
        List<String> keyArray = new ArrayList<>(this.locations.keySet());
        Collections.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return ((Integer.parseInt(s) < Integer.parseInt(t1)) ? -1 : 1);
            }
        });
        for (String tempItem : keyArray) {
            result.add(this.locations.get(tempItem));
        }

        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public String getAsset_type() {
        return asset_type;
    }

    public void setAsset_type(String asset_type) {
        this.asset_type = asset_type;
    }

    public String getMedia_image_url() {
        return media_image_url;
    }

    public void setMedia_image_url(String media_image_url) {
        this.media_image_url = media_image_url;
    }

    public String getMedia_voice_url() {
        return media_voice_url;
    }

    public void setMedia_voice_url(String media_voice_url) {
        this.media_voice_url = media_voice_url;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, Location> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Assets{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", category_id='" + category_id + '\'' +
                ", category_description='" + category_description + '\'' +
                ", asset_type='" + asset_type + '\'' +
                ", media_image_url='" + media_image_url + '\'' +
                ", media_voice_url='" + media_voice_url + '\'' +
                ", locations=" + locations +
                '}';
    }

    public class Location {
        private double latitude;
        private double longitude;

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}

