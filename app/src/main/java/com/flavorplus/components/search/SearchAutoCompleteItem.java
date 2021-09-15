package com.flavorplus.components.search;

public class SearchAutoCompleteItem {
    private String str;
    private int icon;

    public SearchAutoCompleteItem(String str, int icon) {
        this.str = str;
        this.icon = icon;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
