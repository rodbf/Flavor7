package com.flavorplus.recipes.recipedisplay;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.recipes.models.Book;
import com.flavorplus.recipes.bookmarkdialog.BookmarkRepository;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.recipes.models.VoteState;

import java.util.ArrayList;

public class RecipeDisplayViewModel extends AndroidViewModel implements BookmarkRepository.BookmarkRepositoryListener, RecipeRepository.RecipeRepositoryListener {
    public MutableLiveData<Recipe> recipeLiveData = new MutableLiveData<>();
    public MutableLiveData<VoteState> voteState = new MutableLiveData<>();
    private RecipeRepository repo = new RecipeRepository(getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE).getString("jwt", ""), this);
    private BookmarkRepository repoBooks;
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    public RecipeDisplayViewModel(@NonNull Application application) {
        super(application);
        repoBooks = new BookmarkRepository(prefs.getString("jwt", ""), this);
        isLoggedIn.setValue(!prefs.getString("jwt", "").equals(""));
    }

    public void getRecipeById(int id){
        repo.getRecipeById(id);
    }

    public String getImgUrl(){
        return recipeLiveData.getValue().getImgUrl();
    }

    public void setFavorite(boolean fav){
        recipeLiveData.getValue().setFavorite(fav);
        recipeLiveData.setValue(recipeLiveData.getValue());
    }

    public void removeFavorite(){
        repoBooks.removeFavorite(recipeLiveData.getValue().getId());
    }

    @Override
    public void onBooksReceived(ArrayList<Book> list) {

    }

    @Override
    public void onRecipeChanged(Recipe recipe) {
        recipeLiveData.setValue(recipe);
    }

    @Override
    public void onVoteStateChanged(VoteState voteState) {
        Log.d("debugging", "onVoteStateChanged viewModel");
        this.voteState.setValue(voteState);
    }

    @Override
    public void onTokenChanged(String jwt) {
        isLoggedIn.setValue(!jwt.equals(""));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    @Override
    public void onBookmarkAdded(boolean success) {

    }

    @Override
    public void onFavoriteRemoved() {
        setFavorite(false);
    }

    public void vote(int recipeId, int vote){
        repo.vote(recipeId, vote);
    }

    public boolean isLoggedIn(){
        isLoggedIn.setValue(!prefs.getString("jwt", "").equals(""));
        return isLoggedIn.getValue();
    }
}