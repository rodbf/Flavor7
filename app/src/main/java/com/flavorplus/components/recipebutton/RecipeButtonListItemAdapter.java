package com.flavorplus.components.recipebutton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.RecipeButtonBinding;
import com.flavorplus.recipes.models.Recipe;

import java.util.ArrayList;

public class RecipeButtonListItemAdapter extends RecyclerView.Adapter<RecipeButtonListItemAdapter.RecipeButtonListItemViewHolder> {
    static class RecipeButtonListItemViewHolder extends RecyclerView.ViewHolder{

        private RecipeButtonBinding recipeButtonBinding;

        RecipeButtonListItemViewHolder(@NonNull RecipeButtonBinding recipeButtonBinding){
            super(recipeButtonBinding.getRoot());
            this.recipeButtonBinding = recipeButtonBinding;
        }
    }

    public RecipeButtonListItemAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    private ArrayList<Recipe> recipes;
    private FragmentActivity activity;

    @NonNull
    @Override
    public RecipeButtonListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        RecipeButtonBinding recipeButtonBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.recipe_button, viewGroup, false);

        return new RecipeButtonListItemViewHolder(recipeButtonBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeButtonListItemViewHolder recipeButtonListItemViewHolder, int position){
        final Recipe recipe = recipes.get(position);
        recipeButtonListItemViewHolder.recipeButtonBinding.setRecipe(recipe);
        Glide.with(recipeButtonListItemViewHolder.recipeButtonBinding.ivRecipeButton.getContext()).load(recipe.getImgUrl()).placeholder(R.drawable.ic_recipe_placeholder).into(recipeButtonListItemViewHolder.recipeButtonBinding.ivRecipeButton);
        recipeButtonListItemViewHolder.recipeButtonBinding.ivRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationViewModel navigationViewModel = new ViewModelProvider(activity).get(NavigationViewModel.class);
                navigationViewModel.setRecipeId(recipe.getId());
                navigationViewModel.setScreen(NavigationViewModel.Screens.RECIPE_DISPLAY);
            }
        });

    }

    @Override
    public int getItemCount(){
        if(recipes == null)
            return 0;
        return recipes.size();
    }

    public void setRecipeList(ArrayList<Recipe> list){
        this.recipes = list;
        notifyDataSetChanged();
    }

}
