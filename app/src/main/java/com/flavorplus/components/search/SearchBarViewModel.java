package com.flavorplus.components.search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.diet.Diet;
import com.flavorplus.models.Meal;

import java.util.ArrayList;

public class SearchBarViewModel extends AndroidViewModel {
    public SearchBarViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ArrayList<Diet>> diets = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Meal>> meals = new MutableLiveData<>();
    private SearchBarRepository repo = new SearchBarRepository();

    public void getCategories(){
        repo.getCategories(new SearchBarRepository.CategoriesListener() {
            @Override
            public void onMealsChanged(ArrayList<Meal> list) {
                meals.setValue(list);
            }

            @Override
            public void onDietsChanged(ArrayList<Diet> list) {
                diets.setValue(list);
            }
        });
    }
}
