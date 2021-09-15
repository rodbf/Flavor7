package com.flavorplus.register.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.login.repository.LoginResponse;
import com.flavorplus.login.repository.LoginRepository;
import com.flavorplus.register.RegisterForm;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterRepositoryRemote extends RegisterRepository{
    private static final String SERVER = "https://flavorplus.ddns.net:4100";

    public RegisterRepositoryRemote(MutableLiveData<RegisterForm> form, MutableLiveData<Boolean> isRegistering, MutableLiveData<RegisterResponse> responseMutableLiveData){
        super(form, isRegistering, responseMutableLiveData);
    }

    @Override
    public void register() {
        String url = SERVER + "/new-account";

        Map<String, String> params = new HashMap<>();
        params.put("display", form.getValue().getDisplayName());
        params.put("login", form.getValue().getEmail());
        params.put("pass", form.getValue().getPassword());

        VolleySingleton.makeJsonObjectPostRequest(url, "register", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                String json = obj.toString();
                try{
                    RegisterResponse res = new RegisterResponse();
                    res.setRES_CODE(obj.getInt("RES_CODE"));
                    res.setRES_MSG(obj.getString(("RES_MSG")));
                    res.setAUTH(obj.getString("AUTH"));
                    res.setUSER_ID(obj.getInt("USER_ID"));
                    res.setDISPLAY_NAME(obj.getString("DISPLAY_NAME"));
                    res.setACCOUNT_TYPE(obj.getInt("ACCOUNT_TYPE"));
                    res.setEMAIL(obj.getString("EMAIL"));

                    responseMutableLiveData.setValue(res);
                    isRegistering.setValue(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}