package com.flavorplus.recipes.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Book {
    private String name;
    private int id;
    private int followerCount, recipeCount;
    private ArrayList<Recipe> recipes;
    private Recipe[] highlights = new Recipe[3];
    private int ownerId;
    private String ownerName;
    private boolean isDefaultBook;

    public Book() {
    }

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    public void removeRecipe(Recipe recipe){
        this.recipes.remove(recipe);
    }

    public void setHighlight(int position, Recipe recipe){
        this.highlights[position] = recipe;
    }

    public int getRecipeCount() {
        return recipeCount;
    }

    public void setRecipeCount(int recipeCount) {
        this.recipeCount = recipeCount;
    }

    public Recipe getHighlight(int position){
        return highlights[position];
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setDefaultBook(boolean isDefaultBook) {
        this.isDefaultBook = isDefaultBook;
    }
    public boolean isDefaultBook(){
        return isDefaultBook;
    }
}
