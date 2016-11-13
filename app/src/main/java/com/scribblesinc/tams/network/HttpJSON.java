package com.scribblesinc.tams.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by danielmj on 11/11/16.
 */

public class HttpJson {

    private HttpJson() { }

    public static HttpTask requestJSON(int method,
                                       String url,
                                       JsonObject json,
                                       Map<String,String> headers,
                                       final Listener<HttpResponse> resultListener) throws Exception {

        //Translate json to body data
        byte[] body = null;
        if (json != null) {
            String charset = "UTF-8";
            String jsonString = json.toString();
            body = jsonString.getBytes(charset);
        }

        //Override content type
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        HttpRequest request = new HttpRequest(method, url, body, headers, new Listener<HttpResponse>() {
            @Override
            public void onResponse(HttpResponse response) {

                if (response.getError() != null) {
                    System.out.println("ERROR - CANNOT PARSE JSON: " + response.getError().toString());
                }
                else {
                    if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        System.out.println("ERROR - CANNOT PARSE JSON: INVALID RESPONSE CODE [" + response.getResponseCode() + "]");
                    }

                    if (response.getText() != null && response.getText().length() > 0) {
                        try {
                            JsonParser parser = new JsonParser();
                            JsonElement element = parser.parse(response.getText());
                            response.setJsonElement(element);
                        } catch (JsonParseException e) {
                            System.out.println("ERROR - CANNOT PARSE JSON. " + response.getText());
                        }
                    }
                }

                if (resultListener != null) {
                    resultListener.onResponse(response);
                }
            }
        });

        HttpTask task = new HttpTask(request);
        task.execute();
        return task;
    }
}
