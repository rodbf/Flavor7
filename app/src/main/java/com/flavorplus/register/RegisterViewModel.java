package com.flavorplus.register;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.register.repository.RegisterRepository;
import com.flavorplus.register.repository.RegisterRepositoryRemote;
import com.flavorplus.register.repository.RegisterResponse;

public class RegisterViewModel extends AndroidViewModel {
    public MutableLiveData<RegisterForm> form = new MutableLiveData<>();
    public MutableLiveData<Boolean> isRegistering = new MutableLiveData<>();
    public MutableLiveData<RegisterResponse> response = new MutableLiveData<>();
    private RegisterRepository repo = new RegisterRepositoryRemote(form, isRegistering, response);

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        form.setValue(new RegisterForm());
    }

    public void register(){
        if(form.getValue().validateForm()) {
            isRegistering.setValue(true);
            repo.register();
        }
    }

    public boolean processResponse() {
        if(response.getValue().getRES_CODE() == 10010){
            SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("jwt", response.getValue().getAUTH());
            editor.putString("name", response.getValue().getDISPLAY_NAME());
            editor.putInt("account_type", response.getValue().getACCOUNT_TYPE());
            editor.putInt("user_id", response.getValue().getUSER_ID());
            editor.putString("email", response.getValue().getEMAIL());
            editor.apply();
            Toast.makeText(getApplication(), "Registro bem sucedido", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
