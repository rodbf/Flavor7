package com.flavorplus.recipes.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.components.recipebutton.RecipeButton;
import com.flavorplus.components.search.SearchBar;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.search.SearchParameters;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private ExploreViewModel viewModel;
    private NavigationViewModel nav;

    private Observer<SearchParameters> searchParametersObserver;
    private Observer<ArrayList<Recipe>> popularObserver;
    private Observer<ArrayList<Recipe>> breakfastObserver;
    private Observer<ArrayList<Recipe>> lunchObserver;
    private Observer<ArrayList<Recipe>> dinnerObserver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        nav = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        viewModel.getRecipes();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchParametersObserver = new Observer<SearchParameters>() {
            @Override
            public void onChanged(SearchParameters searchParameters) {
                ((SearchBar)getView().findViewById(R.id.search_bar)).setSearchParameters(searchParameters);
            }
        };

        popularObserver = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                LinearLayout ll = getView().findViewById(R.id.ll_popular);
                ll.removeAllViews();
                for(Recipe recipe : recipes){
                    ll.addView(new RecipeButton(recipe).getView(requireActivity(), ll));
                }
            }
        };

        breakfastObserver = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                LinearLayout ll = getView().findViewById(R.id.ll_breakfast);
                ll.removeAllViews();
                for(Recipe recipe : recipes){
                    ll.addView(new RecipeButton(recipe).getView(requireActivity(), ll));
                }
            }
        };

        lunchObserver = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                LinearLayout ll = getView().findViewById(R.id.ll_lunch);
                ll.removeAllViews();
                for(Recipe recipe : recipes){
                    ll.addView(new RecipeButton(recipe).getView(requireActivity(), ll));
                }
            }
        };

        dinnerObserver = new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(ArrayList<Recipe> recipes) {
                LinearLayout ll = getView().findViewById(R.id.ll_dinner);
                ll.removeAllViews();
                for(Recipe recipe : recipes){
                    ll.addView(new RecipeButton(recipe).getView(requireActivity(), ll));
                }
            }
        };

        ((SearchBar)getView().findViewById(R.id.search_bar)).setOnSearchListener(new SearchBar.OnSearchListener() {
            @Override
            public void onSearch(SearchParameters params) {
                nav.setScreen(NavigationViewModel.Screens.SEARCH, params);
            }
        });

        ((SearchBar)getView().findViewById(R.id.search_bar)).getCategories(new SearchBar.OnFinishedLoadingListener() {
            @Override
            public void onFinishedLoading() {
                viewModel.getSearchParameters();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.searchParameters.observe(requireActivity(), searchParametersObserver);
        viewModel.popular.observe(requireActivity(), popularObserver);
        viewModel.breakfast.observe(requireActivity(), breakfastObserver);
        viewModel.lunch.observe(requireActivity(), lunchObserver);
        viewModel.dinner.observe(requireActivity(), dinnerObserver);
    }

    @Override
    public void onDestroy() {
        viewModel.searchParameters.removeObservers(this);
        viewModel.popular.removeObservers(this);
        viewModel.breakfast.removeObservers(this);
        viewModel.lunch.removeObservers(this);
        viewModel.dinner.removeObservers(this);
        super.onDestroy();
    }
}