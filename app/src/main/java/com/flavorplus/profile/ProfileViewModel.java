package com.flavorplus.profile;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.models.User;

public class ProfileViewModel extends AndroidViewModel {
    public MutableLiveData<User> user = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
        User newUser = new User();
        newUser.setName(prefs.getString("name", "username"));
        newUser.setEmail(prefs.getString("email", "email"));
        user.setValue(newUser);
    }
}
