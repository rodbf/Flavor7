package com.flavorplus.diet;

import androidx.annotation.NonNull;

public class Diet {
    public static int VEGETARIAN = 3;
    public static int VEGAN = 1;
    public static int OVOLACTOVEGETARIAN = 2;
    public static int ALL = 0;
    private String dietName;
    private int diet;

    public int getDiet() {
        return diet;
    }

    public void setDiet(int diet) {
        this.diet = diet;
    }

    @NonNull
    @Override
    public String toString() {
        return dietName;
    }

    public String getDietName() {
        return dietName;
    }

    public void setDietName(String dietName) {
        this.dietName = dietName;
    }
}
