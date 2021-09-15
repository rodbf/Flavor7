package com.flavorplus.recipes.bookdisplay;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.BookRecipeBinding;
import com.flavorplus.databinding.LibraryBookBinding;
import com.flavorplus.recipes.models.Book;
import com.flavorplus.recipes.models.Recipe;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> recipes;
    private FragmentActivity activity;
    private RecipeAdapterListener listener;

    public interface RecipeAdapterListener{
        void onRemoveRecipe(int id);
    }

    RecipeListAdapter(FragmentActivity activity, RecipeAdapterListener listener){
        this.activity = activity;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookRecipeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.book_recipe, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.binding.setRecipe(recipe);
        Glide.with(holder.binding.ivRecipeIcon.getContext()).load(recipe.getImgUrl()).placeholder(R.drawable.ic_recipe_placeholder).into(holder.binding.ivRecipeIcon);
        holder.binding.cvRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationViewModel nav = new ViewModelProvider(activity).get(NavigationViewModel.class);
                nav.setRecipeId(recipe.getId());
                nav.setScreen(NavigationViewModel.Screens.RECIPE_DISPLAY);
            }
        });
        holder.binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Remover receita?")
                        .setMessage("Deseja remover a receita \""+ recipe.getName() +"\"?")
                        .setIcon(R.drawable.ic_delete_outline)
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onRemoveRecipe(recipe.getId());
                            }})
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(recipes == null)
            return 0;
        return recipes.size();
    }

    public void setRecipes(ArrayList<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        private BookRecipeBinding binding;

        RecipeViewHolder(@NonNull BookRecipeBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
