package com.flavorplus.recipes.bookmarkdialog;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.flavorplus.R;
import com.flavorplus.recipes.models.Book;

import java.util.ArrayList;

public class BookmarkDialogViewModel extends AndroidViewModel implements BookmarkRepository.BookmarkRepositoryListener {

    private String ERR_NO_BOOK_SELECTED, ERR_INVALID_NAME, ERR_BOOK_EXISTS;

    MutableLiveData<ArrayList<Book>> books = new MutableLiveData<>();
    MutableLiveData<Integer> selectedBook = new MutableLiveData<>();
    MutableLiveData<Boolean> favorited = new MutableLiveData<>();
    MutableLiveData<String> err = new MutableLiveData<>();

    private BookmarkRepository repo;

    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private int recipeId;

    public void setSelectedBook(int pos){
        selectedBook.setValue(pos);
    }

    @Override
    public void onBooksReceived(ArrayList<Book> list) {
        list.add(0, new Book(getApplication().getString(R.string.select_book)+"...", -1));
        list.add(1, new Book("-- "+getApplication().getString(R.string.new_book)+" --", -1));
        books.setValue(list);
    }

    @Override
    public void onTokenChanged(String jwt) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    @Override
    public void onBookmarkAdded(boolean success) {
        if(!success)
            err.setValue(getApplication().getString(R.string.bookmark_write_error));
        else{
            if(selectedBook.getValue()==2)
                favorited.setValue(true);
            else
                favorited.setValue(false);
        }
    }

    @Override
    public void onFavoriteRemoved() {

    }

    public BookmarkDialogViewModel(@NonNull Application application) {
        super(application);

        repo = new BookmarkRepository(prefs.getString("jwt", ""), this);

        ERR_NO_BOOK_SELECTED = getApplication().getString(R.string.ERR_NO_BOOK_SELECTED);
        ERR_INVALID_NAME = getApplication().getString(R.string.ERR_INVALID_NAME);
        ERR_BOOK_EXISTS = getApplication().getString(R.string.ERR_BOOK_ALREADY_EXISTS);
    }

    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }

    public void getUserBooks(){
        repo.getBookList();
    }

    public void selectBook(ArrayList<Book> list, int index, String newBookName){
        if(index == 0){
            err.setValue(ERR_NO_BOOK_SELECTED);
            return;
        }

        if(index == 1){
            if(newBookName.length() < 3) {
                err.setValue(ERR_INVALID_NAME);
                return;
            }
            for(int i = 2; i < list.size(); i++){
                if(list.get(i).getName().equals(newBookName)){
                    selectedBook.setValue(i);
                    err.setValue(ERR_BOOK_EXISTS);
                    return;
                }
            }
            repo.bookmarkToNewBook(recipeId, newBookName);
        }
        else
            repo.addBookmark(recipeId, list.get(index).getId());
    }
}
