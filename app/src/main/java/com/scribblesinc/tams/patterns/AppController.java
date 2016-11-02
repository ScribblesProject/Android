package com.scribblesinc.tams.patterns;

import com.scribblesinc.tams.util.LruBitmapImgCache;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
/*class is a singleton class to initialize core objects of volley library*/
public class AppController extends AppCompatActivity {
    public static final String TAG = AppController.class.getSimpleName();

    //Declare ReuquestQueue and ImageLoader to be use
    private RequestQueue appRequestQueue;
    private ImageLoader appImageLoader;
    //audio
    private static AppController appInstance;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        appInstance = this;
    }
    public static synchronized AppController getInstance(){
        return appInstance;
    }
    public RequestQueue getAppRequestQueue(){
        if(appRequestQueue == null){
            appRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return appRequestQueue;
    }
    public ImageLoader getImageLoader(){
        getAppRequestQueue();
        if(appImageLoader == null){
            appImageLoader = new ImageLoader(this.appRequestQueue, new LruBitmapImgCache());
        }
        return this.appImageLoader;
    }
    public <T> void addToRequestQueue(Request<T> req, String tag){
        //set default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
        getAppRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getAppRequestQueue().add(req);
    }
    public void cancelPendingRequest(Object tag){
        if(appRequestQueue != null){
            appRequestQueue.cancelAll(tag);
        }
    }

}
