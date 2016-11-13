package com.scribblesinc.tams.network;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

/**
 * Created by danielmj on 11/11/16.
 */

public class HttpResponse {
    private String text;
    private int responseCode;
    private Map<String, List<String>> headers;
    private Exception error;
    private JsonElement jsonElement;

    public HttpResponse()
    {
        this.responseCode = -1;
        this.headers = null;
        this.text = null;
        this.error = null;
    }

    public HttpResponse(Exception error) {
        this.error = error;
    }

    public HttpResponse(String text, int responseCode, Map<String, List<String>> headers, Exception error) {
        this.text = text;
        this.responseCode = responseCode;
        this.headers = headers;
        this.error = error;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }
}
