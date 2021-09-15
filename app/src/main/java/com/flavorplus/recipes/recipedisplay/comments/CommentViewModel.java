package com.flavorplus.recipes.recipedisplay.comments;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class CommentViewModel extends AndroidViewModel implements CommentRepository.CommentRepositoryListener {

    public MutableLiveData<String> testString = new MutableLiveData<>();
    public MutableLiveData<ArrayList<Comment>> comments = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    private CommentRepository repo;
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private int recipeId;
    private StringBuilder stringBuilder = new StringBuilder();

    public CommentViewModel(@NonNull Application application) {
        super(application);
        repo = new CommentRepository(this, prefs.getString("jwt", ""));
        isLoggedIn.setValue(!prefs.getString("jwt", "").equals(""));
        Log.d("debugging", "CommentViewModel");
    }

    @Override
    public void onCommentsChanged(ArrayList<Comment> comments) {
        this.comments.setValue(comments);
    }

    @Override
    public void onTestStringChanged(String testString) {
        //this.testString.setValue(testString);
    }

    public void setRecipeId(int recipeId) {
        Log.d("debugging", "CommentViewModel setRecipeId");
        this.recipeId = recipeId;
        repo.getComments(this.recipeId);
    }

    public void createComment(Comment comment) {
        repo.createComment(comment);
    }

    @Override
    public void onTokenChanged(String jwt) {
        isLoggedIn.setValue(!jwt.equals(""));
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    public boolean isLoggedIn(){
        isLoggedIn.setValue(!prefs.getString("jwt", "").equals(""));
        return isLoggedIn.getValue();
    }
}
