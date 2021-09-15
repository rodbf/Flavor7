package com.flavorplus.recipes.recipedisplay.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.R;
import com.flavorplus.databinding.RecyclerItemBinding;
import com.flavorplus.recipes.recipedisplay.model.ListItemParent;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ListItemViewHolder> {
    static class ListItemViewHolder extends RecyclerView.ViewHolder{

        private RecyclerItemBinding recyclerItemBinding;

        ListItemViewHolder(@NonNull RecyclerItemBinding recyclerItemBinding){
            super(recyclerItemBinding.getRoot());
            this.recyclerItemBinding = recyclerItemBinding;
        }
    }

    private ArrayList<ListItemParent> parents;

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType){
        RecyclerItemBinding recyclerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.recycler_item, viewGroup, false);

        return new ListItemViewHolder(recyclerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder listItemViewHolder, int position){
        ListItemParent item = parents.get(position);
        listItemViewHolder.recyclerItemBinding.setItem(item);
    }

    @Override
    public int getItemCount(){
        if(parents == null)
            return 0;
        return parents.size();
    }

    public void setParentList(ArrayList<ListItemParent> list){
        this.parents = list;
        notifyDataSetChanged();
    }

}
