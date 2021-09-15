package com.flavorplus.navbar.unithelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.databinding.UnitHelperDialogBinding;

public class UnitHelperDialogFragment extends DialogFragment {

    private UnitHelperDialogBinding binding;
    private UnitHelperViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = UnitHelperDialogBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UnitHelperViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();
    }
}
