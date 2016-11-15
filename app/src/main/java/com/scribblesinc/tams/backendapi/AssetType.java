package com.scribblesinc.tams.backendapi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.scribblesinc.tams.network.HttpJSON;
import com.scribblesinc.tams.network.HttpResponse;

import java.util.ArrayList;

/**
 * Created by danielmj on 11/14/16.
 */

public class AssetType {

    private long id;
    private String name;
    private long category_id;
    private String category_name;

    public AssetType(long id, String name, long category_id, String category_name) {
        this.id = id;
        this.name = name;
        this.category_id = category_id;
        this.category_name = category_name;
    }

    public static void list(long category_id, final Response.Listener<ArrayList<AssetType>> listener, final Response.ErrorListener errorListener) {
        String url = Assets.hostURL + "api/asset/type/list/" + category_id + "/";
        HttpJson.requestJSON(Request.Method.GET, url, null, null, new Response.Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {

                if (response.getError() != null && errorListener != null) {
                    errorListener.onErrorResponse(new VolleyError(response.getError()));
                    return;
                }

                //Parse all results
                ArrayList<AssetType> result = new ArrayList<AssetType>();
                if (response.getJsonElement() != null) {
                    JsonArray typeArray = response.getJsonElement().getAsJsonObject().getAsJsonArray("types");
                    for (JsonElement category : typeArray) {
                        result.add(mapJSON(category.getAsJsonObject()));
                    }
                }

                //Send it back
                if (listener != null) {
                    listener.onResponse(result);
                }
            }
        });
    }

    private static AssetType mapJSON(JsonObject type)
    {
        if (type == null) {
            return null;
        }

        long id = type.get("id").getAsLong();
        String name = type.get("name").getAsString();
        long category_id = type.get("category-id").getAsLong();
        String category_name = type.get("category-name").getAsString();

        return new AssetType(id, name, category_id, category_name);
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

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "AssetType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category_id=" + category_id +
                ", category_name='" + category_name + '\'' +
                '}';
    }
}
