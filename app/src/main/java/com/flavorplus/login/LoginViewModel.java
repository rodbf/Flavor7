package com.flavorplus.login;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.login.repository.LoginRepository;
import com.flavorplus.login.repository.LoginRepositoryRemote;
import com.flavorplus.login.repository.LoginResponse;
import com.flavorplus.models.User;

import java.util.Objects;

public class LoginViewModel extends AndroidViewModel {
    public MutableLiveData<LoginForm> form = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoggingIn = new MutableLiveData<>();
    public MutableLiveData<LoginResponse> response = new MutableLiveData<>();
    private LoginRepository repo = new LoginRepositoryRemote(form, isLoggingIn, response);

    public LoginViewModel(Application application) {
        super(application);
        form.setValue(new LoginForm());
    }

    public void login(){
        if(Objects.requireNonNull(form.getValue()).validateForm()) {
            isLoggingIn.setValue(true);
            repo.login();
        }
    }

    public boolean processResponse() {
        if(Objects.requireNonNull(response.getValue()).getRES_CODE() == 10000){
            SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("jwt", response.getValue().getAUTH());
            editor.putString("name", response.getValue().getDISPLAY_NAME());
            editor.putInt("account_type", response.getValue().getACCOUNT_TYPE());
            editor.putInt("user_id", response.getValue().getUSER_ID());
            editor.putString("email", response.getValue().getEMAIL());
            editor.apply();
            Toast.makeText(getApplication(), "Login bem sucedido", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(response.getValue().getRES_CODE() == 10004){
            Objects.requireNonNull(form.getValue()).setLoginError("Login/senha errados");
            form.setValue(form.getValue());
        }
        return false;
    }

    public String getLoginResultMessage(){
        int resCode = Objects.requireNonNull(response.getValue()).getRES_CODE();
        if(resCode == 10000){
            return "Bem vindo!";
        }
        if(resCode == 10004 || resCode == 10001){
            return "Login/senha inválidos";
        }
        return "Erro 10020";
    }
}
