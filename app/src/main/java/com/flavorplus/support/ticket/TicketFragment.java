package com.flavorplus.support.ticket;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.databinding.TicketFragmentBinding;
import com.flavorplus.support.models.Ticket;
import com.flavorplus.util.ViewManager;

public class TicketFragment extends Fragment {

    private TicketViewModel viewModel;
    private TicketMessageListAdapter ticketMessagesAdapter;
    private Observer<Ticket> ticketObserver;

    private TicketFragmentBinding binding;
    private NavigationViewModel nav;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = TicketFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(TicketViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        binding.rvMessages.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvMessages.setHasFixedSize(false);
        ticketMessagesAdapter = new TicketMessageListAdapter(requireActivity());
        binding.rvMessages.setAdapter(ticketMessagesAdapter);
        binding.rvMessages.setNestedScrollingEnabled(false);

        ticketObserver = new Observer<Ticket>() {
            @Override
            public void onChanged(Ticket ticket) {
                ticketMessagesAdapter.setTicket(ticket);
            }
        };

        viewModel.getTicket(nav.getTicketId());

        binding.btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etNewTicketMessage.getText().toString();
                if(message.length() <= 1000 && message.length() > 0) {
                    ViewManager.closeKeyboard(v);
                    viewModel.reply(binding.etNewTicketMessage.getText().toString());
                    binding.etNewTicketMessage.setText("");
                }
            }
        });

        binding.btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseTicketDialog();
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
        viewModel.ticket.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.ticket.observe(this, ticketObserver);
    }

    private void showCloseTicketDialog(){
        FragmentManager fm = getParentFragmentManager();
        TicketCloseFragment dialog = new TicketCloseFragment(new TicketCloseFragment.TicketCloseDialogListener() {
            @Override
            public void onConfirm(int solved) {
                viewModel.closeTicket(solved);
                nav.setScreen(NavigationViewModel.Screens.SUPPORT_LANDING);
            }
        });
        dialog.show(fm, "ticket close fragment");
    }
}
