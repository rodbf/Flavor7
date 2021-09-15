package com.flavorplus.suggestions;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import java.util.HashMap;

public class SuggestionsRepository {
    public void sendSuggestion(String jwt, String title, String message) {
        String url = Server.APP_SERVER + "/suggestions/new";
        HashMap<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("subject", title);
        params.put("message", message);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "sendSuggestion", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("debugging", message);
            }

            @Override
            public void onResponse(Object response) {

            }
        });
    }
}
