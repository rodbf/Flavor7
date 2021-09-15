package com.flavorplus.components.recipebutton;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flavorplus.recipes.models.Recipe;

import java.util.Objects;

public class RecipeButtonViewModel extends ViewModel {
    public MutableLiveData<Recipe> recipeButton = new MutableLiveData<>();

    public RecipeButtonViewModel(int id, String name) {
        recipeButton.setValue(new Recipe(id, name));
    }

    public String getName(){
        return Objects.requireNonNull(recipeButton.getValue()).getName();
    }

    public String getImgUrl() {
        return Objects.requireNonNull(recipeButton.getValue()).getImgUrl();
    }

    public int getId(){
        return recipeButton.getValue().getId();
    }

    public Recipe getModel(){
        return recipeButton.getValue();
    }

}
