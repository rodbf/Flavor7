package com.flavorplus.components.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.R;
import com.flavorplus.diet.Diet;
import com.flavorplus.models.Meal;
import com.flavorplus.search.SearchParameters;
import com.flavorplus.util.ViewManager;
import com.flavorplus.util.sqlite.DBManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchBar extends ConstraintLayout {

    public interface OnFinishedLoadingListener{
        void onFinishedLoading();
    }

    private SearchBarViewModel viewModel;

    public interface OnSearchListener{
        void onSearch(SearchParameters params);
    }

    private OnSearchListener onSearchListener = new OnSearchListener() {
        @Override
        public void onSearch(SearchParameters params) {

        }
    };

    public void setOnSearchListener(final OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
        btSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchListener.onSearch(getSearchParameters());
            }
        });
    }

    private TextView tvAdvSearch, tvDuration;
    private Spinner spDiets, spMeals;
    private AppCompatSeekBar sbDuration;
    private AutoCompleteTextView etQuery;
    private Context context;
    private GridLayout glAdvSearch;
    private Button btCancel, btSearch;
    private CheckBox cbRestrictions, cbDislikes;

    private String text = "";
    private int maxDuration;

    private ArrayAdapter<Meal> mealAdapter;
    private ArrayAdapter<Diet> dietAdapter;

    public SearchBar(Context context) {
        super(context);
        this.context = context;
        init();
    }


    public SearchBar(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        text = "";
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SearchBar, 0, 0);

        try{
            text = a.getString(R.styleable.SearchBar_text);
            maxDuration = a.getInteger(R.styleable.SearchBar_max_duration, 60)/5;
        }
        finally{
            a.recycle();
        }
        init();
    }

    private void init() {
        View rootView = inflate(context, R.layout.search_bar, this);
        tvAdvSearch = rootView.findViewById(R.id.tv_adv_search);
        tvDuration = rootView.findViewById(R.id.tv_duration_label);
        spDiets = rootView.findViewById(R.id.sp_diets);
        spMeals = rootView.findViewById(R.id.sp_meals);
        sbDuration = rootView.findViewById(R.id.sb_duration);
        etQuery = rootView.findViewById(R.id.et_search_bar);
        glAdvSearch = rootView.findViewById(R.id.gl_adv_search);
        btCancel = rootView.findViewById(R.id.bt_cancel);
        btSearch = rootView.findViewById(R.id.bt_search);
        cbRestrictions = rootView.findViewById(R.id.cb_restrictions);
        cbDislikes = rootView.findViewById(R.id.cb_dislikes);

        etQuery.setText(text);
        sbDuration.setMax(maxDuration);
        sbDuration.setProgress(maxDuration / 2);
        updateSeekBarLabel();

        tvAdvSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAdvSearchVisibility();
            }
        });

        sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateSeekBarLabel();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ViewManager.closeKeyboard(v);
                    onSearchListener.onSearch(getSearchParameters());
                }
                return true;
            }
        });

        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseAdvSearch();
            }
        });

        SearchAutoCompleteAdapter adapter = new SearchAutoCompleteAdapter(context);
        etQuery.setAdapter(adapter);
    }

    public void getCategories(final OnFinishedLoadingListener listener){
        viewModel = new ViewModelProvider(FragmentManager.findFragment(this)).get(SearchBarViewModel.class);
        viewModel.diets.observe(FragmentManager.findFragment(this), new Observer<ArrayList<Diet>>() {
            @Override
            public void onChanged(ArrayList<Diet> diets) {
                dietAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, diets);
                spDiets.setAdapter(dietAdapter);
                if(mealAdapter != null)
                    listener.onFinishedLoading();
            }
        });
        viewModel.meals.observe(FragmentManager.findFragment(this), new Observer<ArrayList<Meal>>() {
            @Override
            public void onChanged(ArrayList<Meal> meals) {
                mealAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, meals);
                spMeals.setAdapter(mealAdapter);
                if(dietAdapter != null)
                    listener.onFinishedLoading();
            }
        });
        viewModel.getCategories();
    }



    public SearchParameters getSearchParameters(){
        if(etQuery.getText().toString().trim().length() > 0){
            DBManager dbManager = new DBManager(context);
            dbManager.open();
            if(dbManager.fetch(etQuery.getText().toString().trim()).getCount()==0)
                dbManager.insert(etQuery.getText().toString().trim());
            dbManager.close();
        }
        SearchParameters params = new SearchParameters();
        params.setSearchString(etQuery.getText().toString());
        params.setDiet(((Diet)spDiets.getSelectedItem()).getDiet());
        params.setMeal(((Meal)spMeals.getSelectedItem()).getMeal());
        params.setPrepTime(getDuration());
        params.setFilterRestrictions(cbRestrictions.isChecked());
        params.setFilterDownvotes(cbDislikes.isChecked());
        return params;
    }

    public void updateSeekBarLabel(){
        int minutes = getDuration()%60;
        int hours = (getDuration()-minutes)/60;
        tvDuration.setText(hours+minutes==0?"0min":((hours>0?hours+"h":"")+(minutes>0?minutes+"min":"")));
        tvDuration.setX(sbDuration.getX() + sbDuration.getPaddingLeft() + (sbDuration.getWidth() - sbDuration.getPaddingLeft() - sbDuration.getPaddingRight())*sbDuration.getProgress()/sbDuration.getMax() - tvDuration.getWidth()/2);
    }

    public void setDuration(int minutes){
        sbDuration.setProgress(minutes/5);
    }

    public int getDuration(){
        return sbDuration.getProgress()*5;
    }

    private void changeAdvSearchVisibility(){
        if(glAdvSearch.getVisibility() == View.GONE) {
            expandAdvSearch();
        }
        else {
            collapseAdvSearch();
        }
    }

    private void collapseAdvSearch(){
        tvAdvSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_down, 0);
        ViewManager.collapse(glAdvSearch);
    }

    public void expandAdvSearch() {
        tvAdvSearch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_up, 0);
        ViewManager.expand(glAdvSearch);
    }

    public void setSearchParameters(SearchParameters params){
        etQuery.setText(params.getSearchString());
        for(int i = 0; i < spDiets.getCount(); i++){
            if(((Diet)spDiets.getItemAtPosition(i)).getDiet() == params.getDiet().getDiet())
                spDiets.setSelection(i);
        }
        for(int i = 0; i < spMeals.getCount(); i++){
            if(((Meal)spMeals.getItemAtPosition(i)).getMeal() == params.getMeal().getMeal())
                spMeals.setSelection(i);
        }
        cbRestrictions.setChecked(params.filterRestrictions());
        cbDislikes.setChecked(params.filterDownvotes());
        setDuration(params.getPrepTime());
    }


}
