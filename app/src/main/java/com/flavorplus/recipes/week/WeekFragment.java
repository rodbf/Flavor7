package com.flavorplus.recipes.week;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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
import com.flavorplus.R;
import com.flavorplus.databinding.WeekFragmentBinding;
import com.flavorplus.recipes.RecipeIcon;
import com.flavorplus.recipes.models.Day;
import com.flavorplus.recipes.models.Recipe;

import java.util.ArrayList;
import java.util.Objects;

public class WeekFragment extends Fragment {

    private WeekViewModel viewModel;
    private WeekFragmentBinding binding;
    private Observer<ArrayList<Day>> weekObserver;
    private Observer<Recipe> todayObserver;
    private WeekAdapter weekAdapter;
    private NavigationViewModel nav;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = WeekFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WeekViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        binding.rvWeek.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvWeek.setHasFixedSize(false);
        weekAdapter = new WeekAdapter(requireActivity());
        binding.rvWeek.setAdapter(weekAdapter);
        binding.rvWeek.setNestedScrollingEnabled(false);

        weekObserver = new Observer<ArrayList<Day>>() {
            @Override
            public void onChanged(ArrayList<Day> week) {
                weekAdapter.setWeek(week);
            }
        };

        todayObserver = new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                binding.flNextMeal.removeAllViews();
                binding.flNextMeal.addView(RecipeIcon.getView(binding.flNextMeal, recipe));
            }
        };

        binding.llNextMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.setRecipeId(viewModel.nextRecipe.getValue().getId());
                nav.setScreen(NavigationViewModel.Screens.RECIPE_DISPLAY);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        viewModel.week.removeObservers(this);
        viewModel.nextRecipe.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.week.observe(this, weekObserver);
        viewModel.nextRecipe.observe(this, todayObserver);
    }

}
