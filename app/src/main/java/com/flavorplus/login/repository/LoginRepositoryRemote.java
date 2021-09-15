package com.flavorplus.login.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.login.LoginForm;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginRepositoryRemote extends LoginRepository{
    private static final String SERVER = "https://flavorplus.ddns.net:4100";

    public LoginRepositoryRemote(MutableLiveData<LoginForm> form, MutableLiveData<Boolean> isLoggingIn, MutableLiveData<LoginResponse> responseMutableLiveData){
        super(form, isLoggingIn, responseMutableLiveData);
    }

    @Override
    public void login() {
        String url = SERVER + "/login";

        Map<String, String> params = new HashMap<>();
        params.put("login", form.getValue().getEmail());
        params.put("pass", form.getValue().getPassword());

        VolleySingleton.makeJsonObjectPostRequest(url, "login", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    LoginResponse res = new LoginResponse();
                    res.setRES_CODE(obj.getInt("RES_CODE"));
                    res.setRES_MSG(obj.getString(("RES_MSG")));

                    if(res.getRES_CODE() == 10000) {
                        res.setAUTH(obj.getString("AUTH"));
                        res.setUSER_ID(obj.getInt("USER_ID"));
                        res.setDISPLAY_NAME(obj.getString("DISPLAY_NAME"));
                        res.setACCOUNT_TYPE(obj.getInt("ACCOUNT_TYPE"));
                        res.setEMAIL(obj.getString("EMAIL"));
                    }

                    responseMutableLiveData.setValue(res);
                    isLoggingIn.setValue(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}