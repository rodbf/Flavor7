package com.flavorplus;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class NavigationSingleton {

    private static NavigationViewModel instance;

    public static NavigationViewModel getInstance(FragmentActivity activity){
        if (instance == null){
            instance = new ViewModelProvider(activity).get(NavigationViewModel.class);
        }
        return instance;
    }

    public static NavigationViewModel getInstance(){
        return instance;
    }
}
