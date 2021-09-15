package com.flavorplus.util.volley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class ImgController {
    private final String TAG = ImgControllerSingleton.class.getSimpleName();

    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public ImgController(Context c){
        requestQueue = Volley.newRequestQueue(c);
        imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
