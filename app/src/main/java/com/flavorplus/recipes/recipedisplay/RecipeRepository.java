package com.flavorplus.recipes.recipedisplay;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.flavorplus.recipes.models.VoteState;
import com.flavorplus.recipes.recipedisplay.model.ListItemParent;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.Formatter;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class RecipeRepository {
    public interface RecipeRepositoryListener{
        void onRecipeChanged(Recipe recipe);
        void onVoteStateChanged(VoteState voteState);
        void onTokenChanged(String jwt);
    }

    private static final String SERVER = "https://flavorplus.ddns.net:4100";
    private String jwt;
    private RecipeRepositoryListener listener;

    RecipeRepository(@Nullable String jwt, RecipeRepositoryListener listener){
        this.jwt = jwt;
        this.listener = listener;
    }

    void vote(int recipeId, int vote){
        Log.d("debugging", "vote repo");
        String url = SERVER + "/recipe/vote";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("vote", vote+"");
        params.put("recipe_id", recipeId+"");

        Log.d("debugging", params.toString());

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "vote", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("debugging", "vote repo error");
                Log.d("debugging", message);

            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", "vote repo onResponse");
                JSONObject obj = (JSONObject) response;
                try{
                    listener.onTokenChanged(obj.getString("jwt"));
                    JSONObject voteStateJSON = obj.getJSONObject("vote_state");
                    VoteState voteState = new VoteState();
                    voteState.setUpvotes(voteStateJSON.getInt("upvotes"));
                    voteState.setDownvotes(voteStateJSON.getInt("downvotes"));
                    voteState.setVote(voteStateJSON.getInt("vote"));
                    listener.onVoteStateChanged(voteState);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void getRecipeById(int id){
        String url = SERVER + "/pubrecipes/id/" + id;

        Map<String, String> params = new HashMap<>();
        if(jwt != null)
            params.put("jwt", jwt);
        params.put("id", id+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getRecipeById", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                Log.d("debugging", obj.toString());
                try {

                    JSONObject recipe = obj.getJSONObject("recipe");
                    if(obj.has("jwt"))
                        listener.onTokenChanged(obj.getString("jwt"));

                    Recipe newRecipe = new Recipe();
                    newRecipe.setId(recipe.getInt("id"));
                    newRecipe.setVersion(recipe.getDouble("version"));
                    newRecipe.setName(Formatter.toTitleCase(recipe.getString("name")));
                    newRecipe.setDescription(Formatter.capitalize(recipe.getString("description")));
                    newRecipe.setTime(recipe.getInt("time"));
                    newRecipe.setServings(recipe.getInt("servings"));

                    JSONArray ingredients = obj.getJSONArray("ing");

                    if(ingredients.length() > 0){
                        for(int i = 0; i < ingredients.length(); i++){
                            JSONObject ingredient = ingredients.getJSONObject(i);
                            newRecipe.addIngredient(new ListItemParent(ingredient.getInt("position"), ingredient.getString("text")));
                        }
                    }

                    JSONArray steps = obj.getJSONArray("steps");

                    if(steps.length() > 0){
                        for(int i = 0; i < steps.length(); i++){
                            JSONObject step = steps.getJSONObject(i);
                            newRecipe.addStep(new ListItemParent(step.getInt("position"), step.getString("text")));
                        }
                    }

                    newRecipe.setFavorite(obj.getBoolean("isFavorite"));

                    VoteState voteState = new VoteState();

                    voteState.setUpvotes(recipe.getInt("upvotes"));
                    voteState.setDownvotes(recipe.getInt("downvotes"));
                    if(recipe.has("user_vote"))
                        voteState.setVote(recipe.getInt("user_vote"));

                    listener.onRecipeChanged(newRecipe);
                    listener.onVoteStateChanged(voteState);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}