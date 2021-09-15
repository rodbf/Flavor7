package com.flavorplus;

import android.app.Application;

import com.flavorplus.util.volley.ImgController;
import com.flavorplus.util.volley.ImgControllerSingleton;

public class FlavorApplication extends Application {

    private static FlavorApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ImgController getImgController(){
        return ImgControllerSingleton.getInstance(instance);
    }
}