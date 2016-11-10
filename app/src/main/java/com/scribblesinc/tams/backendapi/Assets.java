package com.scribblesinc.tams.backendapi;

import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.scribblesinc.tams.patterns.AppRequestManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

    public ArrayList<Location> sortedLocations(){
        ArrayList<Location> result = new ArrayList<>();
        List<String> keyArray = new ArrayList<>( this.locations.keySet() );
        Collections.sort(keyArray, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return ((Integer.parseInt(s) < Integer.parseInt(t1)) ? -1 : 1);
            }
        });
        for(String tempItem : keyArray){
            result.add(this.locations.get(tempItem));
        }

        return result;
    }

    /// This will fetch all assets in the database
    public static void list(final Response.Listener<ArrayList<Assets>> listener, Response.ErrorListener errorListener) {
        String url = "https://tams-142602.appspot.com/";
        String endpoint = url + "api/asset/list/";
        StringRequest request = new StringRequest(Request.Method.GET, endpoint, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){

                // Decode
                Gson gson  = new Gson();
                Type type = new TypeToken<Map<String,ArrayList<Assets>>>(){}.getType();
                Map<String,ArrayList<Assets>> result = gson.fromJson(response, type);
                ArrayList<Assets> assets = result.get("assets");

                listener.onResponse(assets);

            }
        }, errorListener);

        AppRequestManager.getInstance().addToRequestQueue(request);
    }

    /// This will create a new asset on the server and return an initialized asset object
    public static void create(Response.Listener<Assets> listener, Response.ErrorListener errorListener) {

    }

    /// This will fetch an individual asset
    public static void fetch(Integer assetId, Response.Listener<Assets> listener, Response.ErrorListener errorListener) {

    }

    /// This will upload and attach an image view to the asset
    public void attachImage(ImageView image, Response.Listener<Double> progressListener, Response.ErrorListener errorListener) {

    }

    /// This will upload and attach an voice memo to the asset
    public void attachVoiceMemo(String memoFilePath, Response.Listener<Double> progressListener, Response.ErrorListener errorListener) {

    }

    /// This will update the asset on the server to the current attribute values
    public void update(Response.ErrorListener errorListener) {

    }

    /// This will delete the asset from the server
    public void delete(Response.ErrorListener errorListener) {

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

    public class Location{
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

