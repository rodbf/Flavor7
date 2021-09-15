package com.flavorplus.recipes.explore;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.search.SearchParameters;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreRepository {
    interface ResponseListener{
        void onRecipeListReceived(ArrayList<Recipe> recipes);
    }

    interface TokenChangeListener{
        void onTokenChanged(String jwt);
    }

    interface SearchParametersListener{
        void onSearchParametersReceived(SearchParameters params);
        void onTokenChanged(String jwt);
    }

    private TokenChangeListener tokenChangeListener;

    public ExploreRepository(TokenChangeListener tokenChangeListener) {
        this.tokenChangeListener = tokenChangeListener;
    }

    public void getSearchParameters(String jwt, final SearchParametersListener listener){
        String url = Server.APP_SERVER + "/user/diet";
        HashMap<String, String> params = new HashMap<>();
        params.put("jwt", jwt);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getSearchParameters", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try {
                    if(obj.getInt("RES_CODE") == 10013) {
                        SearchParameters searchParameters = new SearchParameters();
                        searchParameters.setDiet(obj.getInt("DIET"));
                        searchParameters.setFilterRestrictions(true);
                        searchParameters.setFilterDownvotes(true);
                        listener.onSearchParametersReceived(searchParameters);
                        listener.onTokenChanged(obj.getString("jwt"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getRecipeList(ExploreParameters params, final ResponseListener listener){
        String url = Server.APP_SERVER + "/recipes/explore";

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "explore", params.getParams(), new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", response.toString());
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt(("RES_CODE")) == 10013){
                        tokenChangeListener.onTokenChanged(obj.getString("jwt"));

                        ArrayList<Recipe> recipes = new ArrayList<>();
                        JSONArray results = obj.getJSONArray("RESULT");
                        for(int i = 0; i < results.length(); i++){
                            JSONObject row = results.getJSONObject(i);
                            Recipe newRecipe = new Recipe();
                            newRecipe.setId(row.getInt("id"));
                            newRecipe.setName(row.getString("recipe_name"));
                            recipes.add(newRecipe);
                        }
                        listener.onRecipeListReceived(recipes);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
