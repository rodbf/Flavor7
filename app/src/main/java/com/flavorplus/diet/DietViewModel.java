package com.flavorplus.diet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class DietViewModel extends AndroidViewModel {
    public MutableLiveData<ArrayList<Restriction>> restrictions = new MutableLiveData<>();
    public MutableLiveData<String> jwtLiveData = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Restriction>> restrictionsList = new MutableLiveData<>();
    public MutableLiveData<Integer> diet = new MutableLiveData<>();
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private DietRepository repo = new DietRepository(jwtLiveData, restrictionsList, restrictions, diet);

    public DietViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    public void init(){
        restrictions.setValue(new ArrayList<Restriction>());
        restrictionsList.setValue(new ArrayList<Restriction>());
        getRestrictionsList();
        String jwt = prefs.getString("jwt", "");
        getUserRestrictions(jwt);
        getDiet(jwt);
    }

    public void updateDiet(int diet){
        String jwt = prefs.getString("jwt", "");
        Log.d("debugging", "view model diet " + diet);
        repo.updateDiet(jwt, diet);
    }

    public boolean addRestriction(Restriction restriction){
        for(Restriction r : restrictions.getValue()){
            if(r.getId() == restriction.getId())
                return false;
        }
        this.restrictions.getValue().add(restriction);
        this.restrictions.setValue(this.restrictions.getValue());

        String jwt = prefs.getString("jwt", "");
        repo.addRestriction(jwt, restriction.getId());

        return true;
    }

    public void removeRestriction(int id){
        ArrayList<Restriction> restrictions = this.restrictions.getValue();
        for(int i = 0; i < restrictions.size(); i++){
            if(restrictions.get(i).getId() == id) {
                restrictions.remove(i);
                break;
            }
        }
        this.restrictions.setValue(restrictions);

        String jwt = prefs.getString("jwt", "");
        repo.removeRestriction(jwt, id);
    }

    public void getRestrictionsList(){
        repo.getRestrictionsList();
    }

    public void getUserRestrictions(String token){
        repo.getUserRestrictions(token);
    }

    public void getDiet(String token){
        repo.getDiet(token);
    }
}
