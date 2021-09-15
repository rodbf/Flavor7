package com.flavorplus.recipes.recipedisplay.model;

import java.util.ArrayList;
import java.util.Comparator;

public class ListItemParent extends ListItem {
    private ArrayList<ListItem> subList;

    public ArrayList<ListItem> getSubList() {
        return subList;
    }

    public void addSub(ListItem item){
        this.subList.add(item);
        subList.sort(new Comparator<ListItem>(){
            @Override
            public int compare(ListItem lhs, ListItem rhs){
                return Integer.compare(lhs.getPosition(), rhs.getPosition());
            }
        });
    }

    public ListItemParent(int position, String text){
        super(position, text);
        this.subList = new ArrayList<>();
    }
}
