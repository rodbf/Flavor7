package com.flavorplus.models;

import androidx.annotation.NonNull;

public class Meal {
    public static int ANY = 0;
    public static int BREAKFAST = 1;
    public static int LUNCH = 2;
    public static int DINNER = 3;
    private int meal;
    private String mealName;

    public int getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal = meal;
    }

    @NonNull
    @Override
    public String toString() {
        return mealName;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }
}
