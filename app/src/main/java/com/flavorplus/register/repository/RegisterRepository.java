package com.flavorplus.register.repository;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.register.RegisterForm;

public abstract class RegisterRepository {
    MutableLiveData<RegisterForm> form;
    MutableLiveData<Boolean> isRegistering;
    MutableLiveData<RegisterResponse> responseMutableLiveData;

    public abstract void register();

    RegisterRepository(MutableLiveData<RegisterForm> form, MutableLiveData<Boolean> isRegistering, MutableLiveData<RegisterResponse> registerResponseMutableLiveData) {
        this.form = form;
        this.isRegistering = isRegistering;
        this.responseMutableLiveData = registerResponseMutableLiveData;
    }
}
