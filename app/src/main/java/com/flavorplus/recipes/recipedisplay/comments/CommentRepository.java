package com.flavorplus.recipes.recipedisplay.comments;

import android.util.Log;

import com.android.volley.Request;
import com.flavorplus.util.Server;
import com.flavorplus.util.volley.VolleyResponseListener;
import com.flavorplus.util.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentRepository {

    public interface CommentRepositoryListener{
        void onCommentsChanged(ArrayList<Comment> comments);
        void onTestStringChanged(String testString);
        void onTokenChanged(String jwt);
    }

    private CommentRepositoryListener listener;
    private String jwt;
    private int recipeId;
    private Map<Integer, ArrayList<Comment>> childrenMap;

    public CommentRepository(CommentRepositoryListener listener, String jwt) {
        Log.d("debugging", "CommentRepository");
        this.listener = listener;
        this.jwt = jwt;
    }

    public void getComments(int recipeId){
        Log.d("debugging", "getComments");
        childrenMap = new HashMap<>();
        this.recipeId = recipeId;
        String url = Server.APP_SERVER + "/recipe/comments";
        Map<String, String> params = new HashMap<>();
        params.put("recipe_id", recipeId+"");

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "getComments", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("debugging", "onError");
            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", "getComments onResponse");
                JSONObject obj = (JSONObject) response;
                listener.onTestStringChanged(obj.toString());
                try {
                    JSONArray commentsJSON = obj.getJSONArray("COMMENTS");
                    for(int i = 0; i < commentsJSON.length(); i++) {
                        JSONObject row = commentsJSON.getJSONObject(i);
                        int id = row.getInt("id");
                        int parentId = row.getInt("id_parent");
                        String user = row.getString("display_name");
                        String creation = row.getString("creation_timestamp");
                        String content = row.getString("content");
                        Comment newComment = new Comment(id, parentId, user, creation, content, !jwt.equals(""));

                        if(!childrenMap.containsKey(parentId)){
                            childrenMap.put(parentId, new ArrayList<Comment>());
                        }
                        childrenMap.get(parentId).add(newComment);
                    }
                    addChildrenOf(childrenMap.get(0));
                    listener.onCommentsChanged(childrenMap.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addChildrenOf(ArrayList<Comment> root){
        if(root == null)
            return;
        for(Comment c : root){
            c.setChildren(childrenMap.get(c.getId()));
            addChildrenOf(c.getChildren());
        }
    }

    public void createComment(Comment comment) {
        String url = Server.APP_SERVER + "/recipe/new-comment";
        Map<String, String> params = new HashMap<>();
        params.put("parent_id", comment.getParentId()+"");
        params.put("content", comment.getContent());
        params.put("recipe_id", recipeId+"");
        params.put("jwt", jwt);

        VolleySingleton.makeJsonObjectRequest(url, Request.Method.POST, "createComment", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(Object response) {
                Log.d("debugging", "createComment onResponse");
                JSONObject obj = (JSONObject) response;
                try {
                    if (obj.getInt("RES_CODE") == 10013) {
                        jwt = obj.getString("jwt");
                        listener.onTokenChanged(jwt);
                        getComments(recipeId);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}