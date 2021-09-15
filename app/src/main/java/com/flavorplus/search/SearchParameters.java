package com.flavorplus.search;

import com.flavorplus.diet.Diet;
import com.flavorplus.models.Meal;

public class SearchParameters {
    private String searchString = "";
    private Diet diet = new Diet();
    private Meal meal = new Meal();
    private int prepTime = 30;
    private boolean filterRestrictions = false, filterDownvotes = false;



    public Diet getDiet() {
        return diet;
    }

    public void setDiet(int diet) {
        this.diet.setDiet(diet);
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(int meal) {
        this.meal.setMeal(meal);
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public boolean filterRestrictions() {
        return filterRestrictions;
    }

    public void setFilterRestrictions(boolean filterRestrictions) {
        this.filterRestrictions = filterRestrictions;
    }

    public boolean filterDownvotes() {
        return filterDownvotes;
    }

    public void setFilterDownvotes(boolean filterDownvotes) {
        this.filterDownvotes = filterDownvotes;
    }
}
