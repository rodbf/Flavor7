package com.flavorplus.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flavorplus.R;
import com.flavorplus.recipes.models.Recipe;

public class RecipeIcon {
    public static View getView(View parent, Recipe recipe){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_icon, null, false);
        ((TextView)view.findViewById(R.id.tv_icon_name)).setText(recipe.getName());
        Glide.with(parent.getContext()).load(recipe.getImgUrl()).placeholder(R.drawable.ic_recipe_placeholder).into((ImageView)view.findViewById(R.id.iv_recipe_icon));
        return view;
    }
    private RecipeIcon(){

    }
}
