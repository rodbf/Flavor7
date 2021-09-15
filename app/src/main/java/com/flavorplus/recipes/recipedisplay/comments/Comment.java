package com.flavorplus.recipes.recipedisplay.comments;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Comment {
    private int id, parentId;
    private String user, timestamp, content;
    private ArrayList<Comment> children;
    private boolean canReply;

    public boolean canReply() {
        return canReply;
    }

    public void setCanReply(boolean canReply) {
        this.canReply = canReply;
    }

    public Comment() {
    }

    public Comment(int id, int parentId, String user, String timestamp, String content, boolean canReply) {
        this.id = id;
        this.parentId = parentId;
        this.user = user;
        this.timestamp = timestamp;
        this.content = content;
        this.canReply = canReply;
        parseTimeStamp();
    }

    public ArrayList<Comment> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Comment> children) {
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
        parseTimeStamp();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void parseTimeStamp(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat sdfDay = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
            Date date = sdf.parse(this.timestamp);
            Date today = new Date();
            long diff = today.getTime() - date.getTime();
            int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));


            String finalDateString;

            if(numOfDays == 0)
                finalDateString = "Hoje, ";
            else if(numOfDays == 1)
                finalDateString = "Ontem, ";
            else if(numOfDays <= 7)
                finalDateString = numOfDays + " dias atrás, ";
            else
                finalDateString = sdfDay.format(date);
            finalDateString += sdfHour.format(date);

            this.timestamp = finalDateString;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString(){
        return "id: "+id+" parent: "+parentId+" content: "+content;
    }
}
