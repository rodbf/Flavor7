package com.flavorplus.recipes.week;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.WeekDayBinding;
import com.flavorplus.recipes.RecipeIcon;
import com.flavorplus.recipes.models.Day;

import java.util.ArrayList;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DayViewHolder> {
    private ArrayList<Day> week;
    private FragmentActivity activity;

    WeekAdapter(FragmentActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WeekDayBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.week_day, parent, false);
        return new DayViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        final Day day = week.get(position);
        holder.binding.setDay(day);
        ArrayList<FrameLayout> mealFrames = new ArrayList<>();
        mealFrames.add(holder.binding.flMeal1);
        mealFrames.add(holder.binding.flMeal2);
        mealFrames.add(holder.binding.flMeal3);
        final NavigationViewModel nav = new ViewModelProvider(activity).get(NavigationViewModel.class);

        for(int i = 0; i < mealFrames.size(); i++){
            if(day.getMeal(i).getId() != -1){
                mealFrames.get(i).addView(RecipeIcon.getView(mealFrames.get(i), day.getMeal(i)));
                final int finalI = i;
                mealFrames.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nav.setRecipeId(day.getMeal(finalI).getId());
                        nav.setScreen(NavigationViewModel.Screens.RECIPE_DISPLAY);
                    }
                });
            }
        }
    }

    public void setWeek(ArrayList<Day> week){
        this.week = week;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(week == null) {
            return 0;
        }
        return week.size();
    }


    static class DayViewHolder extends RecyclerView.ViewHolder{
        private WeekDayBinding binding;

        DayViewHolder(@NonNull WeekDayBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
