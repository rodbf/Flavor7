package com.flavorplus.recipes.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.flavorplus.recipes.recipedisplay.model.ListItemParent;
import com.flavorplus.util.Server;

import java.util.ArrayList;
import java.util.Comparator;

public class Recipe {
    private int id;
    private double version;
    private String name;
    private String description;
    private int time;
    private int servings;
    private ArrayList<ListItemParent> ingredients;
    private ArrayList<ListItemParent> steps;
    private String imgUrl;
    private boolean isFavorite;

    public Recipe(int id, String name){
        this.id = id;
        this.name = name;
        this.imgUrl = Server.IMAGE_SERVER+"/recipes/images/"+id+"/icon";
    }

    public Recipe() {
        this.id = -1;
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    public Recipe(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
        this.imgUrl = Server.IMAGE_SERVER+"/recipes/images/"+id+"/icon";
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addIngredient(ListItemParent ingredient){
        ingredients.add(ingredient);
        ingredients.sort(new Comparator<ListItemParent>() {
            @Override
            public int compare(ListItemParent lhs, ListItemParent rhs) {
                return Integer.compare(lhs.getPosition(), rhs.getPosition());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addStep(ListItemParent step){
        steps.add(step);
        steps.sort(new Comparator<ListItemParent>() {
            @Override
            public int compare(ListItemParent lhs, ListItemParent rhs) {
                return Integer.compare(lhs.getPosition(), rhs.getPosition());
            }
        });
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ArrayList<ListItemParent> getIngredients() {
        return ingredients;
    }

    public ArrayList<ListItemParent> getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.imgUrl = Server.IMAGE_SERVER+"/recipes/images/"+id+"/icon";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public String toString(){
        String str = "" + ("name: " + name + "\n\ningredients" + "\n");
        for (ListItemParent ing : ingredients)
            str = str + (ing.getText() + "\n");
        str = str + "\nSteps\n";
        for (ListItemParent step : steps)
            str = str + (step.getText() + "\n");
        return str;
    }
}
