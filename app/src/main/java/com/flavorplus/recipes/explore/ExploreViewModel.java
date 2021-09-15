package com.flavorplus.recipes.explore;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.search.SearchParameters;

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel implements ExploreRepository.TokenChangeListener {

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private ExploreRepository repo = new ExploreRepository(this);

    public MutableLiveData<SearchParameters> searchParameters = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Recipe>> popular = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Recipe>> breakfast = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Recipe>> lunch = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Recipe>> dinner = new MutableLiveData<>();

    public ExploreViewModel(@NonNull Application application) {
        super(application);
    }

    public void getSearchParameters(){
        String jwt = prefs.getString("jwt", "");
        if(jwt.equals("")) {
            searchParameters.setValue(new SearchParameters());
            return;
        }
        repo.getSearchParameters(jwt, new ExploreRepository.SearchParametersListener() {
            @Override
            public void onSearchParametersReceived(SearchParameters params) {
                searchParameters.setValue(params);
            }

            @Override
            public void onTokenChanged(String jwt) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("jwt", jwt);
                editor.apply();
            }
        });
    }

    public void getRecipes(){
        ExploreParameters paramsPopular = new ExploreParameters();
        paramsPopular.setJWT(prefs.getString("jwt", ""));
        paramsPopular.setSortMode(ExploreParameters.MODE_SCORE);
        paramsPopular.useDietWhitelist(true);
        paramsPopular.useIngredientBlacklist(true);
        paramsPopular.useTagBlacklist(true);
        paramsPopular.setMaxResults(5);

        repo.getRecipeList(paramsPopular, new ExploreRepository.ResponseListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Recipe> recipes) {
                popular.setValue(recipes);
            }
        });

        ExploreParameters paramsMeal = new ExploreParameters();
        paramsMeal.setJWT(prefs.getString("jwt", ""));
        paramsMeal.useDietWhitelist(true);
        paramsMeal.useIngredientBlacklist(true);
        paramsMeal.useTagBlacklist(true);
        paramsMeal.setMaxResults(5);
        paramsMeal.setSortMode(ExploreParameters.MODE_RANDOM);

        paramsMeal.setMealWhitelistArray(new int[]{1});
        repo.getRecipeList(paramsMeal, new ExploreRepository.ResponseListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Recipe> recipes) {
                breakfast.setValue(recipes);
            }
        });

        paramsMeal.setMealWhitelistArray(new int[]{2});
        repo.getRecipeList(paramsMeal, new ExploreRepository.ResponseListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Recipe> recipes) {
                lunch.setValue(recipes);
            }
        });

        paramsMeal.setMealWhitelistArray(new int[]{3});
        repo.getRecipeList(paramsMeal, new ExploreRepository.ResponseListener() {
            @Override
            public void onRecipeListReceived(ArrayList<Recipe> recipes) {
                dinner.setValue(recipes);
            }
        });

    }

    @Override
    public void onTokenChanged(String jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }
}
