package com.flavorplus.support.landing;

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
import com.flavorplus.support.models.Ticket;

import java.util.ArrayList;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.TicketViewHolder> {
    private ArrayList<Ticket> tickets;
    private FragmentActivity activity;

    TicketListAdapter(FragmentActivity activity){
        this.activity = activity;
    }


    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TicketListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.ticket_list_item, parent, false);
        return new TicketViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        final Ticket ticket = tickets.get(position);
        holder.binding.setModel(ticket);

        holder.binding.cvTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationViewModel nav = new ViewModelProvider(activity).get(NavigationViewModel.class);
                nav.setTicketId(ticket.getId());
                nav.setScreen(NavigationViewModel.Screens.TICKET_DISPLAY);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(tickets == null)
            return 0;
        return tickets.size();
    }

    public void setTickets(ArrayList<Ticket> tickets){
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder{
        private TicketListItemBinding binding;

        TicketViewHolder(@NonNull TicketListItemBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
