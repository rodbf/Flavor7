package com.flavorplus.login.repository;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.login.LoginForm;

public abstract class LoginRepository {
    MutableLiveData<LoginForm> form;
    MutableLiveData<Boolean> isLoggingIn;
    MutableLiveData<LoginResponse> responseMutableLiveData;

    public abstract void login();

    LoginRepository(MutableLiveData<LoginForm> form, MutableLiveData<Boolean> isLoggingIn, MutableLiveData<LoginResponse> responseMutableLiveData) {
        this.form = form;
        this.isLoggingIn = isLoggingIn;
        this.responseMutableLiveData = responseMutableLiveData;
    }
}
