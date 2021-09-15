package com.flavorplus.recipes.librarydisplay;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LibraryRepository {
    public interface LibraryRepositoryListener{
        void onLibraryChanged(ArrayList<Book> books);
        void onTokenChanged(String jwt);
    }

    private LibraryRepositoryListener listener;
    private String jwt;

    public LibraryRepository(String jwt, LibraryRepositoryListener listener) {
        this.jwt = jwt;
        this.listener = listener;
    }

    public void getBookList(boolean ownedBooks){
        String url = Server.APP_SERVER + "/user/books/list-detailed";
        Map<String, String> params = new HashMap<>();
        params.put("jwt", jwt);
        params.put("owned_books", ownedBooks+"");

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

                        Log.d("debugging", obj.toString());

                        JSONArray booksJSON = obj.getJSONArray("books");
                        ArrayList<Book> books = new ArrayList<>();
                        for(int i = 0; i < booksJSON.length(); i++){
                            JSONObject row = booksJSON.getJSONObject(i);
                            Book newBook = new Book();
                            newBook.setId(row.getInt("id"));
                            newBook.setName(row.getString("name"));
                            newBook.setFollowerCount(row.getInt("followers"));
                            newBook.setRecipeCount(row.getInt("recipe_count"));
                            if(!row.isNull("highlights")){
                                JSONArray highlightsJSON = row.getJSONArray("highlights");
                                for(int j = 0; j < highlightsJSON.length() && j < 3; j++){
                                    Recipe newRecipe = new Recipe();
                                    newRecipe.setId(highlightsJSON.getJSONObject(j).getInt("id"));
                                    newRecipe.setName(highlightsJSON.getJSONObject(j).getString("name"));
                                    newBook.setHighlight(j, newRecipe);
                                }
                            }
                            books.add(newBook);
                        }
                        listener.onLibraryChanged(books);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
