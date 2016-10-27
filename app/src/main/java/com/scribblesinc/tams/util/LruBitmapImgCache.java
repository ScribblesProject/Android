package com.scribblesinc.tams.util;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Class to take care of cahing images on disk
 */

public class LruBitmapImgCache extends LruCache<String, Bitmap> implements ImageCache {


    private static int getDefaultLruCacheSize(){

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        return maxMemory/8;//cache size
    }
    public LruBitmapImgCache(){
        this(getDefaultLruCacheSize());
    }
    private LruBitmapImgCache(int sizeInKiloBytes){
        super(sizeInKiloBytes);
    }

    //Returns the size of the entry for key and value
    @Override
    protected int sizeOf(String key, Bitmap value){
        return value.getRowBytes()*value.getHeight()/1024;
    }
    @Override
    public Bitmap getBitmap(String url){
        //Returns value for key if it exists in the cache
        return get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap){
        //caches value for key, value is moved to the head of queue
        put(url, bitmap);
    }

}
