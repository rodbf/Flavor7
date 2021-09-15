package com.flavorplus.support.ticket;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flavorplus.support.TicketsRepository;
import com.flavorplus.support.models.Message;
import com.flavorplus.support.models.Ticket;

import java.util.ArrayList;

public class TicketViewModel extends AndroidViewModel {
    public MutableLiveData<Ticket> ticket = new MutableLiveData<>();
    private SharedPreferences prefs = getApplication().getSharedPreferences("userData", Context.MODE_PRIVATE);
    private TicketsRepository repo = new TicketsRepository();

    private TicketsRepository.TicketListener listener = new TicketsRepository.TicketListener() {
        @Override
        public void onTicketChanged(Ticket newTicket) {
            ticket.setValue(newTicket);
        }

        @Override
        public void onTokenChanged(String jwt) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("jwt", jwt);
            editor.apply();
        }
    };

    public TicketViewModel(@NonNull Application application) {
        super(application);
    }

    public void getTicket(int id){
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0){
            repo.getTicket(jwt, id, listener);
        }
    }

    public void reply(String message) {
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0){
            int id = ticket.getValue().getId();
            repo.reply(jwt, id, message, listener);
        }
    }

    public void closeTicket(int solved) {
        String jwt = prefs.getString("jwt", "");
        if(jwt.length() > 0){
            int id = ticket.getValue().getId();
            repo.closeTicket(jwt, id, solved);
        }
    }
}
