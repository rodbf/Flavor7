package com.flavorplus.support.ticket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.TicketListItemBinding;
import com.flavorplus.databinding.TicketMessageBinding;
import com.flavorplus.support.models.Message;
import com.flavorplus.support.models.Ticket;

import java.util.ArrayList;

public class TicketMessageListAdapter extends RecyclerView.Adapter<TicketMessageListAdapter.TicketViewHolder> {
    private Ticket ticket;
    private FragmentActivity activity;

    TicketMessageListAdapter(FragmentActivity activity){
        this.activity = activity;
    }


    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TicketMessageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ticket_message, parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Message message = ticket.getMessages().get(position);
        holder.binding.setModel(message);
    }

    @Override
    public int getItemCount() {
        if(ticket == null)
            return 0;
        return ticket.getMessages().size();
    }

    public void setTicket(Ticket ticket){
        this.ticket = ticket;
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder{
        private TicketMessageBinding binding;

        TicketViewHolder(@NonNull TicketMessageBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
