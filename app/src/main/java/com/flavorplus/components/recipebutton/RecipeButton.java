package com.flavorplus.components.recipebutton;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.RecipeButtonBinding;
import com.flavorplus.recipes.models.Recipe;

public class RecipeButton {
    private RecipeButtonViewModel viewModel;
    private NavigationViewModel navigationViewModel;

    public RecipeButton(int id, String name){
        viewModel = new RecipeButtonViewModel(id, name);
    }

    public RecipeButton(Recipe recipe) {
        viewModel = new RecipeButtonViewModel(recipe.getId(), recipe.getName());
    }

    public String getName(){
        return viewModel.getName();
    }

    public View getView(final FragmentActivity context, ViewGroup parent){
        navigationViewModel = new ViewModelProvider(context).get(NavigationViewModel.class);
        RecipeButtonBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.recipe_button, parent, false);
        binding.setLifecycleOwner(context);
        binding.setRecipe(viewModel.getModel());
        View root = binding.getRoot();
        Glide.with(parent.getContext()).load(viewModel.getImgUrl()).placeholder(R.drawable.ic_recipe_placeholder).into((ImageView)root.findViewById(R.id.iv_recipe_button));
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationViewModel.setRecipeId(viewModel.getId());
                navigationViewModel.setScreen(NavigationViewModel.Screens.RECIPE_DISPLAY);
            }
        });
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) binding.clRecipeButton.getLayoutParams();
        lp.width = (int)(0.4 * metrics.widthPixels);
        binding.clRecipeButton.setLayoutParams(lp);
        return root;
    }
}
