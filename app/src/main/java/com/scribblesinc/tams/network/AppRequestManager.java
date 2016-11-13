package com.scribblesinc.tams.network;

import com.scribblesinc.tams.util.LruBitmapImgCache;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
/*class is a singleton class to initialize core objects of volley library*/
public class AppRequestManager extends Application {

    public static final String TAG = AppRequestManager.class.getSimpleName();

    //Declare ReuquestQueue and ImageLoader to be use
    private RequestQueue appRequestQueue;
    private ImageLoader appImageLoader;
    //audio
    private static AppRequestManager appInstance;

    @Override
    public void onCreate(){
        super.onCreate();
        appInstance = this;
    }

    public static AppRequestManager getInstance() {

        if (appInstance == null) {
            synchronized (AppRequestManager.class) {
                if (appInstance == null) {
                    appInstance = new AppRequestManager();
                }
            }
        }

        return appInstance;
    }

    public RequestQueue getAppRequestQueue(){
        if(appRequestQueue == null) {
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
        if(appRequestQueue != null) {
            appRequestQueue.cancelAll(tag);
        }
    }

}
