package com.flavorplus.components.search;

import android.util.Log;

import com.flavorplus.diet.Diet;
import com.flavorplus.models.Meal;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchBarRepository {
    public interface CategoriesListener{
        void onMealsChanged(ArrayList<Meal> meals);
        void onDietsChanged(ArrayList<Diet> diets);
    }

    public void getCategories(final CategoriesListener listener){
        String url = Server.APP_SERVER + "/categories";

        VolleySingleton.makeJsonObjectGetRequest(url, "getCategories", new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try {
                    JSONArray dietsJson = obj.getJSONArray("diets");
                    ArrayList<Diet> diets = new ArrayList<>();
                    for(int i = 0; i < dietsJson.length(); i++){
                        Diet newDiet = new Diet();
                        newDiet.setDiet(dietsJson.getJSONObject(i).getInt("id"));
                        newDiet.setDietName(dietsJson.getJSONObject(i).getString("diet_name"));
                        diets.add(newDiet);
                    }
                    listener.onDietsChanged(diets);

                    JSONArray mealsJson = obj.getJSONArray("meals");
                    ArrayList<Meal> meals = new ArrayList<>();
                    Meal defaultMeal = new Meal();
                    defaultMeal.setMeal(0);
                    defaultMeal.setMealName("Todas");
                    meals.add(defaultMeal);
                    for(int i = 0; i < mealsJson.length(); i++){
                        Meal newMeal = new Meal();
                        newMeal.setMeal(mealsJson.getJSONObject(i).getInt("id"));
                        newMeal.setMealName(mealsJson.getJSONObject(i).getString("meal_name"));
                        meals.add(newMeal);
                    }
                    listener.onMealsChanged(meals);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
