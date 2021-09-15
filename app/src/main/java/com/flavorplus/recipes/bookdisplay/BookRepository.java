package com.flavorplus.recipes.bookdisplay;

import android.util.AtomicFile;
import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.recipes.models.Book;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookRepository {

    public interface LibraryRepositoryListener{
        void onBookChanged(Book book);
        void onTokenChanged(String jwt);
    }

    private LibraryRepositoryListener listener;
    private String jwt;

    public BookRepository(String jwt, LibraryRepositoryListener listener) {
        this.jwt = jwt;
        this.listener = listener;
    }

    public void getBook(int id){
        Log.d("debugging", "getBook "+id);

        String url = Server.APP_SERVER + "/user/books/detail";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("id", id+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getBook", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                JSONObject obj = (JSONObject) response;
                Log.d("obj", obj.toString());
                try{
                    if(obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);

                        Book newBook = new Book();
                        newBook.setId(obj.getInt("book_id"));
                        newBook.setName(obj.getString("book_name"));
                        newBook.setOwnerId(obj.getInt("owner_id"));
                        newBook.setOwnerName(obj.getString("owner_name"));
                        newBook.setDefaultBook(obj.getInt("is_default") == 1);

                        if(!obj.isNull("recipes")) {
                            JSONArray recipesJSON = obj.getJSONArray("recipes");
                            ArrayList<Recipe> recipes = new ArrayList<>();
                            for (int i = 0; i < recipesJSON.length(); i++) {
                                JSONObject row = recipesJSON.getJSONObject(i);
                                Recipe newRecipe = new Recipe();
                                newRecipe.setId(row.getInt("id"));
                                newRecipe.setName(row.getString("name"));
                                recipes.add(newRecipe);
                            }
                            newBook.setRecipes(recipes);
                        }

                        listener.onBookChanged(newBook);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }


    public void removeRecipe(int recipeId, int bookId) {
        Log.d("debugging", "removeRecipe "+recipeId+" "+bookId);

        String url = Server.APP_SERVER + "/user/books/remove-recipe";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("id_recipe", recipeId+"");
        params.put("id_book", bookId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "removeRecipe", params, new VolleyResponseListener() {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void editBookName(String name, int id) {
        Log.d("debugging", "editName "+name+" "+id);

        String url = Server.APP_SERVER + "/user/books/update-name";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("book_name", name);
        params.put("book_id", id+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "editBookName", params, new VolleyResponseListener() {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void deleteBook(int id) {
        Log.d("debugging", "deleteBook "+id);

        String url = Server.APP_SERVER + "/user/books/delete";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("id", id+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "deleteBook", params, new VolleyResponseListener() {
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
