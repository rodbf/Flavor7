package com.flavorplus.login.repository;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.login.LoginForm;

import java.util.ArrayList;

public class LoginRepositoryLocal extends LoginRepository {
    private ArrayList<String> emails;
    private String email, pass;

    public LoginRepositoryLocal(MutableLiveData<LoginForm> form, MutableLiveData<Boolean> isLoggingIn, MutableLiveData<LoginResponse> responseMutableLiveData){
        super(form, isLoggingIn, responseMutableLiveData);
        emails = new ArrayList<>();
        emails.add("test@test.com");
        emails.add("test@test.test");
    }

    @Override
    public void login() {
        email = form.getValue().getEmail();
        pass = form.getValue().getPassword();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoggingIn.setValue(false);
                if(emails.contains(email)){
                    form.getValue().addEmailError("e-mail já cadastrado");
                }
            }
        }, 1000);
    }
}
