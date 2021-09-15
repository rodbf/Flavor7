package com.flavorplus.support.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

public class Ticket {
    private int id;
    private String date;
    private String title;
    private int status;
    private ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String formatToList() {
        String[] statuses = new String[]{"Esperando resposta", "Respondido", "Fechado"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/YY HH:mm");
        String formattedTime = "";
        try {
            Date d = sdf.parse(date);
            formattedTime = output.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "Assunto: " + title + "\nCriado em " + formattedTime + "\nSituação atual: " + statuses[status];
    }

    public String formatToTitle(){
        return "Ticket #"+id;
    }
}
