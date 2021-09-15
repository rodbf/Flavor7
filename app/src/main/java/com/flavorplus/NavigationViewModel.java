package com.flavorplus;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends AndroidViewModel {

    public MutableLiveData<NavigationEvent> navigationController = new MutableLiveData<>();
    private int recipeId = 1;
    private String searchStr;
    private int currentScreen;
    private int bookId, ticketId;
    private boolean addtoBackStack = true;
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);

    public NavigationViewModel(@NonNull Application application) {
        super(application);
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        this.searchStr = searchStr;
    }

    public void setScreen(int screen, Object parameters, boolean addToBackStack){
        this.addtoBackStack = addToBackStack;
        if((screen == Screens.WEEK) && prefs.getString("jwt", "").length() < 1){
            currentScreen = Screens.LOGIN;
            navigationController.setValue(new NavigationEvent(currentScreen));
        }
        else {
            currentScreen = screen;
            navigationController.setValue(new NavigationEvent(currentScreen, parameters));
        }
    }

    public void setScreen(int screen, Object parameters){
        setScreen(screen, parameters, true);
    }

    public void setScreen(int screen){
        setScreen(screen, null, true);
    }

    public void setScreen(int screen, boolean addToBackStack){
        setScreen(screen, null, addToBackStack);
    }

    public boolean getAddtoBackStack() {
        return addtoBackStack;
    }

    public void setAddtoBackStack(boolean addtoBackStack) {
        this.addtoBackStack = addtoBackStack;
    }

    public int getCurrentScreen(){
        return currentScreen;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setTicketId(int id) {
        this.ticketId = id;
    }

    public int getTicketId(){
        return ticketId;
    }

    public class NavigationEvent{
        private boolean handled = false;
        private int targetScreen;
        private Object parameters = null;

        NavigationEvent(int targetScreen){
            this.targetScreen = targetScreen;
        }

        NavigationEvent(int targetScreen, Object parameters){
            this.targetScreen = targetScreen;
            this.parameters = parameters;
        }

        public int getTargetScreen() {
            return targetScreen;
        }

        public Object getParameters() {
            return parameters;
        }

        public boolean wasHandled() {
            return handled;
        }

        public void setHandled(boolean handled) {
            this.handled = handled;
        }
    }

    public static class Screens{
        public static final int HOME = 0;
        public static final int RECIPE_DISPLAY = 1;
        public static final int EXPLORE = 2;
        public static final int SEARCH = 3;
        public static final int LOGIN = 4;
        public static final int PROFILE = 5;
        public static final int REGISTER = 6;
        public static final int DIET = 7;
        public static final int LIBRARY = 8;
        public static final int BOOKTEST = 1000;
        public static final int WEEK = 10;
        public static final int COMMENT_TEST = 1001;
        public static final int SUPPORT_LANDING = 11;
        public static final int TICKET_DISPLAY = 12;
        public static final int SUGGESTIONS = 13;
    }
}
