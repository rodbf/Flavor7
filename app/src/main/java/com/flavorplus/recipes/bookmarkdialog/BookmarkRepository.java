package com.flavorplus.recipes.bookmarkdialog;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.recipes.models.Book;
import com.flavorplus.diet.Restriction;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookmarkRepository {

    public interface BookmarkRepositoryListener{
        void onBooksReceived(ArrayList<Book> list);
        void onTokenChanged(String jwt);
        void onBookmarkAdded(boolean success);
        void onFavoriteRemoved();
    }

    private static final String SERVER = "https://flavorplus.ddns.net:4100";

    private BookmarkRepositoryListener listener;
    private String jwt;

    public BookmarkRepository(String jwt, BookmarkRepositoryListener listener) {
        this.jwt = jwt;
        this.listener = listener;
    }

    public void setRepoListener(BookmarkRepositoryListener listener) {
        this.listener = listener;
    }

    public void getBookList(){
        String url = SERVER + "/user/books/list-basic";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getBookList", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);

                        JSONArray booksJSON = obj.getJSONArray("BOOKS");
                        ArrayList<Book> list = new ArrayList<>();
                        for(int i = 0; i < booksJSON.length(); i++){
                            Restriction newRestriction = new Restriction();
                            JSONObject row = booksJSON.getJSONObject(i);
                            list.add(new Book(row.getString("book_name"), row.getInt("id")));
                        }
                        listener.onBooksReceived(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void bookmarkToNewBook(int recipeId, String newBookName) {
        String url = SERVER + "/user/books/new-book";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("book_name", newBookName);
        params.put("recipe_id", recipeId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "bookmarkNewBook", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);
                        listener.onBookmarkAdded(true);
                    }
                    else
                        listener.onBookmarkAdded(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onBookmarkAdded(false);
                }

            }
        });
    }

    public void addBookmark(int recipeId, int bookId) {
        String url = SERVER + "/user/books/new-bookmark";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("book_id", bookId+"");
        params.put("recipe_id", recipeId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "addBookmark", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", "onResponse\n"+response.toString());
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);
                        listener.onBookmarkAdded(true);
                    }
                    else
                        listener.onBookmarkAdded(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onBookmarkAdded(false);
                }

            }
        });
    }

    public void removeFavorite(int recipeId) {
        String url = SERVER + "/user/books/remove-favorite";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("recipe_id", recipeId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "addBookmark", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", "onResponse\n"+response.toString());
                JSONObject obj = (JSONObject) response;
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);
                        listener.onFavoriteRemoved();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
