package com.flavorplus.support.landing;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.databinding.SupportFragmentBinding;
import com.flavorplus.support.models.Ticket;
import com.flavorplus.util.ViewManager;

import java.util.ArrayList;

public class SupportFragment extends Fragment {

    private SupportViewModel viewModel;
    private TicketListAdapter ticketsAdapter;
    private Observer<ArrayList<Ticket>> ticketsObserver;

    private SupportFragmentBinding binding;
    private NavigationViewModel nav;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SupportFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SupportViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        binding.rvTickets.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvTickets.setHasFixedSize(false);
        ticketsAdapter = new TicketListAdapter(requireActivity());
        binding.rvTickets.setAdapter(ticketsAdapter);
        binding.rvTickets.setNestedScrollingEnabled(false);

        ticketsObserver = new Observer<ArrayList<Ticket>>() {
            @Override
            public void onChanged(ArrayList<Ticket> tickets) {
                ticketsAdapter.setTickets(tickets);
            }
        };

        viewModel.getTickets();

        binding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.etNewTicketTitle.getText().toString();
                String message = binding.etNewTicketMessage.getText().toString();
                if(title.length() <= 50 && title.length() > 0 && message.length() <= 1000 && message.length() > 0) {
                    ViewManager.closeKeyboard(v);
                    viewModel.createTicket(title, message);
                    binding.etNewTicketTitle.setText("");
                    binding.etNewTicketMessage.setText("");
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        viewModel.ticketList.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.ticketList.observe(this, ticketsObserver);
    }
}
