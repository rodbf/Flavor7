package com.flavorplus.recipes.week;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.recipes.models.Day;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeekRepository {

    public interface WeekRepositoryListener{
        void onWeekChanged(ArrayList<Day> week);
        void onTokenChanged(String jwt);
        void setTestString(String test);
    }

    private WeekRepositoryListener listener;
    private String jwt;

    public WeekRepository(String jwt, WeekRepositoryListener listener){
        this.jwt = jwt;
        this.listener = listener;
    }

    public void getWeek(){
        Log.d("debugging", "getWeek");
        String url = Server.APP_SERVER + "/user/suggestions/week";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getWeek", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("debugging", "getWeek error " + message);
            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", response.toString());
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013){
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);

                        ArrayList<Day> week = new ArrayList<>();
                        for(int i = 0; i < 7; i++){
                            Day newDay = new Day();
                            ArrayList<Recipe> meals = new ArrayList<>();
                            for(int j = 0; j < 3; j++){
                                meals.add(new Recipe());
                            }
                            newDay.setMeals(meals);
                            week.add(newDay);
                        }

                        JSONArray recipesJSON = obj.getJSONArray("RECIPES");
                        for(int i = 0; i < recipesJSON.length(); i++){
                            JSONObject row = recipesJSON.getJSONObject(i);
                            Day day = week.get(row.getInt("day_of_week")-1);
                            day.setDay(row.getInt("day_of_week"));
                            day.setMeal(row.getInt("id_meal") - 1, row.getInt("id_recipe"), row.getString("recipe_name"));
                        }

                        listener.onWeekChanged(week);

                        StringBuilder stringBuilder = new StringBuilder();

                        for(int i = 0; i < week.size(); i++){
                            for(int j = 0; j < week.get(i).getMeals().size(); j++){
                                stringBuilder.append(i).append(" ").append(j).append(" ").append(week.get(i).getMeal(j).getId()).append("\n");
                            }
                        }

                        String testString = stringBuilder.toString();
                        listener.setTestString(testString);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
