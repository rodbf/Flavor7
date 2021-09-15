package com.flavorplus.recipes.week;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.recipes.models.Day;
import com.flavorplus.recipes.models.Recipe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class WeekViewModel extends AndroidViewModel implements WeekRepository.WeekRepositoryListener{
    public WeekViewModel(@NonNull Application application) {
        super(application);
        WeekRepository repo = new WeekRepository(prefs.getString("jwt", ""), this);
        repo.getWeek();
    }

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    public MutableLiveData<ArrayList<Day>> week = new MutableLiveData<>();
    public MutableLiveData<String> testString = new MutableLiveData<>();
    public MutableLiveData<String> today = new MutableLiveData<>();
    public MutableLiveData<String> nextMeal = new MutableLiveData<>();
    public MutableLiveData<Recipe> nextRecipe = new MutableLiveData<>();


    @Override
    public void onWeekChanged(ArrayList<Day> week){
        this.week.setValue(week);

        String[] days = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo"};
        String[] meals = {"Café da manhã", "Almoço", "Jantar"};

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK) - 2;
        if(day < 0)
            day = 6;
        int hour = c.get(Calendar.HOUR_OF_DAY);
        Log.d("debugging", day + " " + hour);

        this.today.setValue(days[day]);

        int meal;
        if(hour > 14){
            meal = 2;
        }
        else if(hour > 9){
            meal = 1;
        }
        else{
            meal = 0;
        }
        this.nextMeal.setValue(meals[meal]);
        this.nextRecipe.setValue(this.week.getValue().get(day).getMeal(meal));
    }

    @Override
    public void onTokenChanged(String jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    @Override
    public void setTestString(String test) {
        testString.setValue(test);
    }

}
