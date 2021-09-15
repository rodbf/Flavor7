package com.flavorplus.recipes.models;

import java.util.ArrayList;

public class Day {
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THRUSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 7;



    private ArrayList<Recipe> meals;
    private String day;

    public ArrayList<Recipe> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Recipe> meals) {
        this.meals = meals;
    }

    public Recipe getMeal(int pos){
        return meals.get(pos);
    }

    public void setMeal(int pos, int recipeId, String recipeName){
        meals.get(pos).setId(recipeId);
        meals.get(pos).setName(recipeName);
    }

    public void setDay(int day){
        switch(day){
            case MONDAY:
                this.day = "Segunda-feira";
                break;
            case TUESDAY:
                this.day = "Terça-feira";
                break;
            case WEDNESDAY:
                this.day = "Quarta-feira";
                break;
            case THRUSDAY:
                this.day = "Quinta-feira";
                break;
            case FRIDAY:
                this.day = "Sexta-feira";
                break;
            case SATURDAY:
                this.day = "Sábado";
                break;
            case SUNDAY:
                this.day = "Domingo";
                break;
        }
    }

    public String getDay(){
        return day;
    }
}
