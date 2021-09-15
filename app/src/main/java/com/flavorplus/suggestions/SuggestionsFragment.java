package com.flavorplus.suggestions;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.google.android.material.textfield.TextInputEditText;

public class SuggestionsFragment extends Fragment {

    private SuggestionsViewModel viewModel;
    private NavigationViewModel nav;
    private TextInputEditText title, message;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(SuggestionsViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        return inflater.inflate(R.layout.suggestions_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = getView().findViewById(R.id.et_suggestion_title);
        message = getView().findViewById(R.id.et_suggestion_message);
        Button btSend = getView().findViewById(R.id.bt_send);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleStr = title.getText().toString();
                String messageStr = message.getText().toString();
                if(titleStr.length() > 0 && titleStr.length() <= 50 && messageStr.length() > 0 && messageStr.length() <= 1000) {
                    if(viewModel.sendSuggestion(titleStr, messageStr)) {
                        nav.setScreen(NavigationViewModel.Screens.HOME);
                        Toast.makeText(requireContext(), "Obrigado!", Toast.LENGTH_LONG).show();
                    }
                    else
                        nav.setScreen(NavigationViewModel.Screens.LOGIN);
                }
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
