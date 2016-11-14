package com.scribblesinc.tams.backendapi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scribblesinc.tams.network.HttpJson;
import com.scribblesinc.tams.network.HttpResponse;
import com.scribblesinc.tams.network.HttpTask;

import java.util.ArrayList;

/**
 * Created by danielmj on 11/14/16.
 */

public class AssetCategory {
    private long id;
    private String name;
    private String description;

    public AssetCategory(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static void list(final Response.Listener<ArrayList<AssetCategory>> listener, final Response.ErrorListener errorListener)
    {
        String url = Assets.hostURL + "api/asset/category/list/";
        HttpTask task = HttpJson.requestJSON(Request.Method.GET, url, null, null, new Response.Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {
                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                ArrayList<AssetCategory> result = new ArrayList<AssetCategory>();
                if (response.getJsonElement() != null) {
                    JsonArray categoryArray = response.getJsonElement().getAsJsonObject().getAsJsonArray("categories");
                    for (JsonElement category : categoryArray)
                    {
                        result.add( mapJSON( category.getAsJsonObject() ) );
                    }
                }

                //Send it back
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
    }

    private static AssetCategory mapJSON( JsonObject category )
    {
        if (category == null) {
            return null;
        }

        long id = category.get("id").getAsLong();
        String name = category.get("name").getAsString();
        String description = category.get("description").getAsString();

        return new AssetCategory(id, name, description);
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

    @Override
    public String toString() {
        return "AssetCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
