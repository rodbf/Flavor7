package com.flavorplus.diet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.R;
import com.flavorplus.databinding.DietRestrictionRecyclerItemBinding;

import java.util.ArrayList;

public class RestrictionItemAdapter extends RecyclerView.Adapter<RestrictionItemAdapter.RestrictionItemViewHolder> {

    private ArrayList<Restriction> restrictions;
    private DietViewModel dietViewModel;

    public RestrictionItemAdapter(DietViewModel dietViewModel) {
        this.dietViewModel = dietViewModel;
    }

    @NonNull
    @Override
    public RestrictionItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        DietRestrictionRecyclerItemBinding dietRestrictionRecyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.diet_restriction_recycler_item, viewGroup, false);
        return new RestrictionItemViewHolder(dietRestrictionRecyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestrictionItemViewHolder holder, final int position) {
        final Restriction item = restrictions.get(position);
        holder.dietRestrictionRecyclerItemBinding.setItem(item);
        holder.dietRestrictionRecyclerItemBinding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dietViewModel.removeRestriction(item.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return restrictions.size();
    }

    public void setRestrictionList(ArrayList<Restriction> list){
        this.restrictions = list;
        notifyDataSetChanged();
    }

    static class RestrictionItemViewHolder extends RecyclerView.ViewHolder{
        private DietRestrictionRecyclerItemBinding dietRestrictionRecyclerItemBinding;
        RestrictionItemViewHolder(@NonNull DietRestrictionRecyclerItemBinding dietRestrictionRecyclerItemBinding) {
            super(dietRestrictionRecyclerItemBinding.getRoot());
            this.dietRestrictionRecyclerItemBinding = dietRestrictionRecyclerItemBinding;
        }
    }
}
