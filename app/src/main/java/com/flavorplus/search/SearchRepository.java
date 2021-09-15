package com.flavorplus.search;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.flavorplus.components.recipebutton.RecipeButtonModel;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchRepository {
    public void getSearchResults(String jwt, SearchParameters searchParameters, final SearchListener listener) {
        String url = Server.APP_SERVER + "/pubrecipes/search";
        HashMap<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("search_str", searchParameters.getSearchString());
        params.put("diet", searchParameters.getDiet().getDiet()+"");
        params.put("meal", searchParameters.getMeal().getMeal()+"");
        params.put("filter_restrictions", searchParameters.filterRestrictions()+"");
        params.put("filter_downvotes", searchParameters.filterDownvotes()+"");
        params.put("prep_time", searchParameters.getPrepTime()+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getSearchResults", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;

                try {
                    if(obj.getInt("RES_CODE") == 10013) {
                        if(obj.has("jwt")){
                            listener.onTokenChanged(obj.getString("jwt"));
                        }
                        JSONArray results = obj.getJSONArray("result");
                        ArrayList<Recipe> recipes = new ArrayList<>();
                        for(int i = 0; i < results.length(); i++){
                            JSONObject row = results.getJSONObject(i);
                            Recipe newRecipe = new Recipe();
                            newRecipe.setName(row.getString("recipe_name"));
                            newRecipe.setId(row.getInt("id"));
                            recipes.add(newRecipe);
                        }
                        listener.onResultChanged(recipes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public interface SearchListener{
        void onResultChanged(ArrayList<Recipe> recipes);
        void onTokenChanged(String jwt);
    }



    public void getRecipesByName(String name, final SearchListener listener){
        String url = Server.APP_SERVER + "/pubrecipes/name/" + name;
        VolleySingleton.makeJsonArrayGetRequest(url, "recipes", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONArray array = (JSONArray) response;

                ArrayList<Recipe> newList = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        Recipe newRecipe = new Recipe();
                        newRecipe.setId(obj.getInt("id"));
                        newRecipe.setName(obj.getString("name"));
                        newList.add(newRecipe);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                listener.onResultChanged(newList);
            }
        });
    }
}