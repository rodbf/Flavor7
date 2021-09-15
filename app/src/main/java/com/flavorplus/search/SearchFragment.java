package com.flavorplus.search;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.R;
import com.flavorplus.components.DelayAutoCompleteTextView;
import com.flavorplus.components.recipebutton.RecipeButtonListItemAdapter;
import com.flavorplus.components.recipebutton.RecipeButtonModel;
import com.flavorplus.components.search.SearchBar;
import com.flavorplus.databinding.SearchFragmentBinding;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.util.ViewManager;
import com.flavorplus.util.sqlite.DBManager;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private SearchFragmentBinding binding;
    private SearchParameters params;

    public SearchFragment(SearchParameters params){
        this.params = params;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SearchFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView resultView = binding.rvSearchResults;

        resultView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        resultView.setHasFixedSize(true);
        final RecipeButtonListItemAdapter resultsAdapter = new RecipeButtonListItemAdapter(getActivity());
        resultView.setAdapter(resultsAdapter);

        viewModel.results.observe(this.requireActivity(), new Observer<ArrayList<Recipe>>(){
            @Override
            public void onChanged(ArrayList<Recipe> recipes){
                resultsAdapter.setRecipeList(recipes);
            }
        });

        String searchStr = params.getSearchString();
        ((AutoCompleteTextView)binding.sbSearchResults.findViewById(R.id.et_search_bar)).setText(searchStr);
        getResults();

        ((AutoCompleteTextView)binding.sbSearchResults.findViewById(R.id.et_search_bar)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                search();
                return true;
            }
        });

        binding.sbSearchResults.findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        binding.sbSearchResults.getCategories(new SearchBar.OnFinishedLoadingListener() {
            @Override
            public void onFinishedLoading() {
                binding.sbSearchResults.setSearchParameters(params);
                search();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void getResults(){
    }

    private void search(){
        ViewManager.closeKeyboard(binding.clSearch);
        binding.clSearch.clearFocus();
        viewModel.getSearchResults(binding.sbSearchResults.getSearchParameters());

        DBManager dbManager = new DBManager(getContext());
        dbManager.open();
        String str = ((DelayAutoCompleteTextView)binding.sbSearchResults.findViewById(R.id.et_search_bar)).getText().toString().trim();
        if(dbManager.fetch(str).getCount()==0)
            dbManager.insert(str);
        dbManager.close();
    }

}
