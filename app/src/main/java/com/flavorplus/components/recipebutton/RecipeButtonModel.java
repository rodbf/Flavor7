package com.flavorplus.components.recipebutton;

import com.flavorplus.util.Server;

public class RecipeButtonModel {
    private String name, imgUrl;
    private int id;

    public RecipeButtonModel(int id, String name) {
        this.id = id;
        this.name = name;
        this.imgUrl = Server.IMAGE_SERVER+"/recipes/images/"+id+"/icon";
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getId() {
        return id;
    }
}
