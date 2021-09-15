package com.flavorplus.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.components.recipebutton.RecipeButton;
import com.flavorplus.databinding.HomeFragmentBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private NavigationViewModel nav;
    private ArrayList<RecipeButton> recipeButton = new ArrayList<>();
    private HomeFragmentBinding binding;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        binding.btWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.setScreen(NavigationViewModel.Screens.WEEK);
            }
        });

        binding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.setScreen(NavigationViewModel.Screens.EXPLORE);
            }
        });
    }



}
