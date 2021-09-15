package com.flavorplus.support.landing;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flavorplus.support.TicketsRepository;
import com.flavorplus.support.models.Ticket;

import java.util.ArrayList;

public class SupportViewModel extends AndroidViewModel{
    public MutableLiveData<ArrayList<Ticket>> ticketList = new MutableLiveData<>();
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private TicketsRepository repo = new TicketsRepository();

    private TicketsRepository.TicketsListListener listener = new TicketsRepository.TicketsListListener() {
        @Override
        public void onListChanged(ArrayList<Ticket> tickets) {
            ticketList.setValue(tickets);
        }

        @Override
        public void onTokenChanged(String jwt) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("jwt", jwt);
            editor.apply();
        }
    };

    public SupportViewModel(@NonNull Application application) {
        super(application);
    }

    public void getTickets(){
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0) {
            repo.getTickets(jwt, listener);
        }
    }

    public void createTicket(String title, String message){
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0){
            repo.createTicket(jwt, title, message, listener);
        }
    }
}
