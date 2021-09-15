package com.flavorplus.search;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flavorplus.R;
import com.flavorplus.components.DelayAutoCompleteTextView;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.sqlite.DBManager;

import java.util.ArrayList;

public class SearchViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Recipe>> results = new MutableLiveData<>();
    private SearchRepository repo = new SearchRepository();
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);

    public SearchViewModel(@NonNull Application application) {
        super(application);
    }

    public void getSearchResults(SearchParameters searchParameters) {
        addToHistory(searchParameters.getSearchString().trim());
        String jwt = prefs.getString("jwt", "");
        repo.getSearchResults(jwt, searchParameters, new SearchRepository.SearchListener(){
            @Override
            public void onResultChanged(ArrayList<Recipe> recipes) {
                results.setValue(recipes);
            }

            @Override
            public void onTokenChanged(String jwt) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("jwt", jwt);
                editor.apply();
            }
        });
    }

    private void addToHistory(String str){
        DBManager dbManager = new DBManager(getApplication());
        dbManager.open();
        if(dbManager.fetch(str).getCount()==0)
            dbManager.insert(str);
        dbManager.close();

    }
}
