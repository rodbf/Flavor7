package com.flavorplus.recipes.librarydisplay;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.recipes.models.Book;

import java.util.ArrayList;

public class LibraryViewModel extends AndroidViewModel implements LibraryRepository.LibraryRepositoryListener {
    public LibraryViewModel(@NonNull Application application) {
        super(application);
        repo = new LibraryRepository(prefs.getString("jwt", ""), this);
        repo.getBookList(true);
    }

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);

    private String title = "Meus Livros";
    private LibraryRepository repo;
    public MutableLiveData<ArrayList<Book>> books = new MutableLiveData<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onLibraryChanged(ArrayList<Book> books) {
        this.books.setValue(books);
    }

    @Override
    public void onTokenChanged(String jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }
}
