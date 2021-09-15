package com.flavorplus.recipes.bookdisplay;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.recipes.models.Book;
import com.flavorplus.recipes.models.Recipe;

import java.util.ArrayList;

public class BookViewModel extends AndroidViewModel implements BookRepository.LibraryRepositoryListener {
    public BookViewModel(@NonNull Application application) {
        super(application);
        repo = new BookRepository(prefs.getString("jwt", ""), this);
    }

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);

    private BookRepository repo;
    public MutableLiveData<Book> book = new MutableLiveData<>();

    public void setBook(int id){
        repo.getBook(id);
    }

    @Override
    public void onBookChanged(Book book) {
        this.book.setValue(book);
    }

    @Override
    public void onTokenChanged(String jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    public void removeRecipe(int id) {
        Log.d("debugging", "recipe id "+id);
        ArrayList<Recipe> recipes = book.getValue().getRecipes();
        for(Recipe recipe : recipes){
            if(recipe.getId() == id) {
                recipes.remove(recipe);
                break;
            }
        }
        book.setValue(book.getValue());
        repo.removeRecipe(id, book.getValue().getId());
    }

    public void setBookName(String name) {
        Book newBook = book.getValue();
        newBook.setName(name);
        book.setValue(newBook);
        Log.d("debugging", "setBookName "+name);

        repo.editBookName(name, book.getValue().getId());
    }

    public void deleteBook() {
        repo.deleteBook(book.getValue().getId());
    }
}
