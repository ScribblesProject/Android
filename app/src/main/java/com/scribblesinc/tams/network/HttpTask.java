package com.scribblesinc.tams.network;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by danielmj on 11/12/16.
 */

public class HttpTask extends AsyncTask<HttpRequest, Double, HttpResponse> {

    HttpRequest request;
    HttpURLConnection connection;

    double uploadProgress = 0.0;
    double downloadProgress = 0.0;

    /**
     * Specifies upload flush interval
     * AND specifies upload/download progress reporting interval
     * */
    final int chunkSizeBytes = 128;

    public HttpTask(HttpRequest request) {
        this.request = request;
    }

    @Override
    protected HttpResponse doInBackground(HttpRequest... httpRequests) {
        HttpResponse result = null;
//        System.out.println("[TASK] STARTING CONNECTION");

        try {
            String response = performConnection();
            result = new HttpResponse(
                    response,
                    connection.getResponseCode(),
                    connection.getHeaderFields(),
                    null
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            result = new HttpResponse(e);
        }
        finally {
            disconnect();
        }

        return result;
    }

    @Override
    protected void onPostExecute(HttpResponse response) {
        super.onPostExecute(response);

        if (request.responseListener != null) {
            request.responseListener.onResponse(response);
        }
    }

    @Override
    protected void onProgressUpdate(Double... values) {
        super.onProgressUpdate(values);

        double newUploadProgress = values[0];
        double newDownloadProgress = values[1];

        if (uploadProgress != newUploadProgress && request.uploadProgressListener != null) {
            request.uploadProgressListener.onResponse(newUploadProgress);
        }

        if (downloadProgress != newDownloadProgress && request.downloadProgressListener != null) {
            request.downloadProgressListener.onResponse(newDownloadProgress);
        }

        uploadProgress = newUploadProgress;
        downloadProgress = newDownloadProgress;
    }

    public String performConnection() throws Exception
    {
        validate();

        // SETUP ----------
        connection = (HttpURLConnection) new URL(request.url).openConnection();
        connection.setRequestMethod(methodToString(request.method));
        if (request.headers != null) {
            for (String key : request.headers.keySet()) {
                connection.setRequestProperty(key, request.headers.get(key));
            }
        }


        // SEND ------------
        if (request.body != null && request.body.length > 0)
        {
            connection.setRequestProperty("Content-length", request.body.length + "");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            writeStream(connection.getOutputStream());
        }
        connection.connect();


        // RESPONSE -----------
        String response;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            response = readStream(connection.getInputStream());
        }
        else {
            response = readStream(connection.getErrorStream());
        }

        return response;
    }

    private void writeStream(OutputStream stream) throws Exception
    {
        publishProgress(0.0, 0.0);

        int totalBytes = 0;
        for (byte b : request.body) {
            totalBytes += 1;
            stream.write(b);
            if (totalBytes % chunkSizeBytes == 0) {
                stream.flush();

                //Send upload progress
                if (request.body.length > 0) {
                    double progress = (double)totalBytes / (double)request.body.length;
                    publishProgress(progress, 0.0);
                }
            }
        }
        stream.flush();
        stream.close();

        publishProgress(1.0, 0.0);
    }

    private String readStream(InputStream stream) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer response = new StringBuffer();
        int contentLength = connection.getContentLength();

        publishProgress(1.0, 0.0);

        int bytesRead = 0;
        int lastChar = reader.read();
        while (lastChar != -1) {
            bytesRead += 1;
            response.append((char)lastChar);

            if (contentLength > 0 && bytesRead % chunkSizeBytes == 0) {
                double progress = (double)bytesRead / (double)contentLength;
                publishProgress(1.0, progress);
            }

            lastChar = reader.read();
        }

        reader.close();

        publishProgress(1.0,1.0);

        return response.toString();
    }

    private void validate() throws Exception
    {
        if (methodToString(request.method).length() == 0) {
            throw new Exception("Invalid Method");
        }

        if (request.url == null || request.url.length() == 0) {
            throw new Exception("Invalid URL");
        }
    }

    private void disconnect()
    {
        if (connection != null)
        {
            connection.disconnect();
        }
    }

    private static String methodToString(int method) {
        switch (method) {
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.DELETE:
                return "DELETE";
            case Request.Method.HEAD:
                return "HEAD";
            case Request.Method.OPTIONS:
                return "OPTIONS";
            case Request.Method.TRACE:
                return "TRACE";
            case Request.Method.PATCH:
                return "PATCH";
            default:
                return "";
        }
    }

    public void setDownloadProgressListener(Response.Listener<Double> downloadProgressListener) {
        request.setDownloadProgressListener(downloadProgressListener);
    }

    public void setUploadProgressListener(Response.Listener<Double> uploadProgressListener) {
        request.setUploadProgressListener( uploadProgressListener );
    }
}
