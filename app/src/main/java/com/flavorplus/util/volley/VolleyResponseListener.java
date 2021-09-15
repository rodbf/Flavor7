package com.flavorplus.util.volley;

public interface VolleyResponseListener {
    void onError(String message);
    void onResponse(Object response);
}
