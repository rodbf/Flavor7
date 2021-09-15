package com.flavorplus.support.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String date;
    private String user;
    private String message;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String formatHeader(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/YY HH:mm");
        String formattedTime = "";
        try {
            Date d = sdf.parse(date);
            formattedTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return user + " - " + formattedTime;

    }
}
