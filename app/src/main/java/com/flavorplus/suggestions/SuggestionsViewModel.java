package com.flavorplus.suggestions;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SuggestionsViewModel extends AndroidViewModel {

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private SuggestionsRepository repo = new SuggestionsRepository();

    public SuggestionsViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean sendSuggestion(String titleStr, String messageStr) {
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0) {
            repo.sendSuggestion(jwt, titleStr, messageStr);
            return true;
        }
        else return false;
    }
    // TODO: Implement the ViewModel
}
