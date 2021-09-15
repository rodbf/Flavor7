package com.flavorplus.recipes.recipedisplay.model;

public class ListItem {
    private String text;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ListItem(int position, String text) {
        this.position = position;
        this.text = text;
    }
}
