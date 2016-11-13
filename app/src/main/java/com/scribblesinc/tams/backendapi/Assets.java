package com.scribblesinc.tams.backendapi;

import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.scribblesinc.tams.androidcustom.Locations;
import com.scribblesinc.tams.network.AppRequestManager;
import com.scribblesinc.tams.network.HttpJson;
import com.scribblesinc.tams.network.HttpResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

    private static final String hostURL = "https://tams-142602.appspot.com/";
    private static final String TAG = Assets.class.getSimpleName();

    private long id;
    private String name;
    private String description;
    private String category;
    private String category_id;
    private String category_description;
    private String asset_type;
    private String media_image_url;
    private String media_voice_url;
    private Map<String, AssetLocation> locations;

    private Assets(long id, String name, String description, String category, String category_id, String category_description, String asset_type, String media_image_url, String media_voice_url, Map<String, AssetLocation> locations) {
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

    public ArrayList<AssetLocation> getSortedLocations() {
        ArrayList<AssetLocation> result = new ArrayList<>();
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
    public static void list(final Listener<ArrayList<Assets>> listener, final ErrorListener errorListener) {

        String url = hostURL + "api/asset/list/";
        HttpJson.requestJSON(Request.Method.GET, url, null, null, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                ArrayList<Assets> result = new ArrayList<Assets>();
                if (response.getJsonElement() != null) {
                    JsonArray assetArray = response.getJsonElement().getAsJsonObject().getAsJsonArray("assets");
                    for (JsonElement asset : assetArray)
                    {
                        result.add( mapJSON( asset.getAsJsonObject() ) );
                    }
                }

                //Send it back
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
    }

    /// This will create a new asset on the server and return the id of the asset created. -1 if not successfull.
    public static void create(String name, String description, String category, String categoryDescription, String typeName, ArrayList<AssetLocation> sortedAssetLocations, final Listener<Long> listener, final ErrorListener errorListener)
    {
        String url = hostURL + "api/asset/create/";
        JsonObject json = createJSON(name, description, category, categoryDescription, typeName, sortedAssetLocations);

        HttpJson.requestJSON(Request.Method.GET, url, json, null, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                long result = -1;
                if (response.getJsonElement() != null) {
                    JsonObject status = response.getJsonElement().getAsJsonObject();
                    if (status.get("success").getAsBoolean())
                    {
                        result = status.get("id").getAsLong();
                    }
                }

                //Send it back
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
    }

    /// This will fetch an individual asset
    public static void fetch(long assetId, final Listener<Assets> listener, final ErrorListener errorListener) {

        String url = hostURL + "api/asset/" + assetId + "/";
        HttpJson.requestJSON(Request.Method.GET, url, null, null, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                Assets result = null;
                if (response.getJsonElement() != null) {
                    result = mapJSON( response.getJsonElement().getAsJsonObject() );
                }

                //Send it back
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
    }

    /// This will upload and attach an image view to the asset
    public void attachImage(ImageView image, Listener<Double> progressListener, Listener<Boolean> responseListener,  ErrorListener errorListener) {

        // I may need some help with these

    }

    /// This will upload and attach an voice memo to the asset
    public void attachVoiceMemo(String memoFilePath, Listener<Double> progressListener, Listener<Boolean> responseListener, ErrorListener errorListener) {

        // I may need some help with these

    }

    /// This will update the asset on the server to the current attribute values
    public void update(final Listener<Boolean> responseListener, final ErrorListener errorListener) {
        String url = hostURL + "api/asset/update/" + this.id + "/";
        JsonObject json = createJSON(this.name, this.description, this.category, this.category_description, this.asset_type, this.getSortedLocations());

        System.out.println("ERROR JSON? " + json.toString());

        HttpJson.requestJSON(Request.Method.PUT, url, json, null, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                boolean result = false;
                System.out.println("ERROR? " + response.getText());
                if (response.getJsonElement() != null) {
                    JsonObject status = response.getJsonElement().getAsJsonObject();
                    if (status.get("success").getAsBoolean())
                    {
                        result = true;
                    }
                }

                //Send it back
                if (responseListener != null) {
                    responseListener.onResponse(result);
                }
            }
        });
    }

    /// This will delete the asset from the server
    public void delete(final Listener<Boolean> responseListener, final ErrorListener errorListener) {
        String url = hostURL + "api/asset/delete/" + this.id + "/";

        HttpJson.requestJSON(Request.Method.DELETE, url, null, null, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                boolean result = false;
                if (response.getJsonElement() != null) {
                    JsonObject status = response.getJsonElement().getAsJsonObject();
                    if (status.get("success").getAsBoolean())
                    {
                        result = true;
                    }
                }

                //Send it back
                if (responseListener != null) {
                    responseListener.onResponse(result);
                }
            }
        });
    }

    public static Assets mapJSON(JsonObject asset)
    {
        if (asset == null) {
            return null;
        }

        long id                     = asset.get("id").getAsLong();
        String name                 = asset.get("name").getAsString();
        String description          = asset.get("description").getAsString();
        String category             = asset.get("category").getAsString();
        String category_id          = asset.get("category-id").getAsString();
        String category_description = asset.get("category-description").getAsString();
        String asset_type           = asset.get("asset-type").getAsString();
        String media_image_url      = asset.get("media-image-url").getAsString();
        String media_voice_url      = asset.get("media-voice-url").getAsString();

        Map<String, AssetLocation> locations = new HashMap<String, AssetLocation>();
        JsonObject locationsObject      = asset.get("locations").getAsJsonObject();
        for(Map.Entry<String, JsonElement> item : locationsObject.entrySet())
        {
            String orderString = item.getKey();
            JsonObject loc = item.getValue().getAsJsonObject();
            double latitude = loc.get("latitude").getAsDouble();
            double longitude = loc.get("longitude").getAsDouble();
            locations.put(orderString, new AssetLocation(latitude,longitude));
        }

        return new Assets(id,
                name,
                description,
                category,
                category_id,
                category_description,
                asset_type,
                media_image_url,
                media_voice_url,
                locations);
    }

    public static JsonObject createJSON(String name, String description, String category, String categoryDescription, String typeName, ArrayList<AssetLocation> sortedAssetLocations)
    {
        try
        {
            JsonObject result = new JsonObject();

            result.addProperty("name", name);
            result.addProperty("description", description);
            result.addProperty("category",category);
            result.addProperty("category-description", categoryDescription);
            result.addProperty("type-name", typeName);

            JsonObject locations = new JsonObject();
            Integer currentOrder = 0;
            for (AssetLocation value : sortedAssetLocations) {
                JsonObject loc = new JsonObject();
                loc.addProperty("longitude", value.getLongitude());
                loc.addProperty("latitude", value.getLatitude());

                currentOrder += 1;
                String order = currentOrder.toString();
                locations.add(order, loc);
            }
            result.add("locations", locations);

            return result;
        }
        catch (Exception e)
        {
            System.out.println("ERROR PARSING JSON : " + e.toString());
            return null;
        }
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

    public Map<String, AssetLocation> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, AssetLocation> locations) {
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
}

