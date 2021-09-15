package com.flavorplus.util.volley;

import android.content.Context;

public class ImgControllerSingleton {
    private static ImgController instance;

    public static ImgController getInstance(Context c){
        if(instance == null){
            instance = new ImgController(c);
        }
        return instance;
    }
}
