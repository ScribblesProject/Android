package com.scribblesinc.tams.network;

import com.android.volley.Response;

import java.util.Map;

/**
 * Created by danielmj on 11/12/16.
 */

public class HttpRequest {
    int method;
    String url;
    byte[] body;
    Map<String, String> headers;
    Response.Listener<Double> downloadProgressListener;
    Response.Listener<Double> uploadProgressListener;
    Response.Listener<HttpResponse> responseListener;

    public HttpRequest(int method,
                       String url,
                       byte[] body,
                       Map<String, String> headers,
                       Response.Listener<HttpResponse> responseListener) {
        this.method = method;
        this.url = url;
        this.body = body;
        this.headers = headers;
        this.downloadProgressListener = null;
        this.uploadProgressListener = null;
        this.responseListener = responseListener;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Response.Listener<HttpResponse> getResponseListener() {
        return responseListener;
    }

    public void setResponseListener(Response.Listener<HttpResponse> responseListener) {
        this.responseListener = responseListener;
    }

    public Response.Listener<Double> getDownloadProgressListener() {
        return downloadProgressListener;
    }

    public void setDownloadProgressListener(Response.Listener<Double> downloadProgressListener) {
        this.downloadProgressListener = downloadProgressListener;
    }

    public Response.Listener<Double> getUploadProgressListener() {
        return uploadProgressListener;
    }

    public void setUploadProgressListener(Response.Listener<Double> uploadProgressListener) {
        this.uploadProgressListener = uploadProgressListener;
    }
}
