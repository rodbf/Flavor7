package com.flavorplus.register.repository;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;

import com.flavorplus.register.RegisterForm;

import java.util.ArrayList;

public class RegisterRepositoryLocal extends RegisterRepository {
    private ArrayList<String> names, emails;
    private String name, email, pass;

    public RegisterRepositoryLocal(MutableLiveData<RegisterForm> form, MutableLiveData<Boolean> isRegistering, MutableLiveData<RegisterResponse> responseMutableLiveData) {
        super(form, isRegistering, responseMutableLiveData);
        names = new ArrayList<>();
        emails = new ArrayList<>();
        names.add("test name");
        names.add("rodrigo");
        names.add("flavor7");
        emails.add("test@test.com");
        emails.add("test@test.test");
    }

    @Override
    public void register() {
        name = form.getValue().getDisplayName();
        email = form.getValue().getEmail();
        pass = form.getValue().getPassword();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRegistering.setValue(false);
                if(names.contains(name)){
                    form.getValue().addDisplayNameError("nome já cadastrado");
                }
                if(emails.contains(email)){
                    form.getValue().addEmailError("e-mail já cadastrado");
                }
            }
        }, 1000);
    }
}
