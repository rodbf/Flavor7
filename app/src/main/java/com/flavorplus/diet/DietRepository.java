package com.flavorplus.diet;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DietRepository {
    private static final String SERVER = "https://flavorplus.ddns.net:4100";
    private MutableLiveData<String> jwt;
    private MutableLiveData<ArrayList<Restriction>> restrictionsList;
    private MutableLiveData<ArrayList<Restriction>> userRestrictions;
    private MutableLiveData<Integer> diet;


    public DietRepository(MutableLiveData<String> jwt, MutableLiveData<ArrayList<Restriction>> restrictionsList, MutableLiveData<ArrayList<Restriction>> userRestrictions, MutableLiveData<Integer> diet) {
        this.jwt = jwt;
        this.restrictionsList = restrictionsList;
        this.userRestrictions = userRestrictions;
        this.diet = diet;
    }

    public void updateDiet(String token, final int dietId) {

        String url = SERVER + "/user/diet/update";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", token);
        params.put("diet", dietId+"");

        VolleySingleton.makeJsonObjectPostRequest(url,"update diet", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt.setValue(obj.getString("jwt"));
                        diet.setValue(dietId);
                        Log.d("debugging", "repo update diet " + dietId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getDiet(String token) {

        String url = SERVER + "/user/diet";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", token);

        VolleySingleton.makeJsonObjectPostRequest(url,"get diet", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt.setValue(obj.getString("jwt"));
                        diet.setValue(obj.getInt("DIET"));

                        Log.d("debugging", "repo get diet " + obj.getInt("DIET"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void addRestriction(String token, int restrictionId) {

        String url = SERVER + "/user/diet/restriction/add";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", token);
        params.put("id_tag", restrictionId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "add restriction", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt.setValue(obj.getString("jwt"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeRestriction(String token, int restrictionId) {

        String url = SERVER + "/user/diet/restriction/remove";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", token);
        params.put("id_tag", restrictionId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "add restriction", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt.setValue(obj.getString("jwt"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void getRestrictionsList(){
        String url = SERVER + "/tags";

        VolleySingleton.makeJsonArrayGetRequest(url, "get restrictions", new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONArray array = (JSONArray) response;

                ArrayList<Restriction> newList = new ArrayList<>();

                for(int i = 0; i < array.length(); i++){
                    try {
                        JSONObject obj = array.getJSONObject(i);
                        newList.add(new Restriction(obj.getInt("id"), obj.getString("tag_name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                restrictionsList.setValue(newList);

            }
        });
    }

    public void getUserRestrictions(String token){
        String url = SERVER + "/user/diet/restriction/list";

        Map<String, String> params = new HashMap<>();
        params.put("jwt", token);

        VolleySingleton.makeJsonObjectPostRequest(url, "get user restrictions", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt.setValue(obj.getString("jwt"));
                        JSONArray restrictions = obj.getJSONArray("TAGS");
                        ArrayList<Restriction> dbUserRestrictions = new ArrayList<>();
                        for(int i = 0; i < restrictions.length(); i++){
                            Restriction newRestriction = new Restriction();
                            JSONObject row = restrictions.getJSONObject(i);
                            newRestriction.setId(row.getInt("id_tag"));
                            newRestriction.setName(row.getString("tag_name"));
                            dbUserRestrictions.add(newRestriction);
                        }
                        userRestrictions.setValue(dbUserRestrictions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
