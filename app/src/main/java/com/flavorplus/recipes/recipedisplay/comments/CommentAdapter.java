package com.flavorplus.recipes.recipedisplay.comments;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flavorplus.R;
import com.flavorplus.databinding.RecipeCommentBinding;
import com.flavorplus.util.ViewManager;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private ArrayList<Comment> comments;
    private CommentListener listener;

    public CommentAdapter(CommentListener listener) {
        this.listener = listener;
    }

    public interface CommentListener{
        void onSendReply(Comment reply);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecipeCommentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.recipe_comment, parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentAdapter.CommentViewHolder holder, final int position) {
        holder.binding.setComment(comments.get(position));
        ViewManager.collapse(holder.binding.clReply);
        View.OnClickListener collapseOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean visible = holder.binding.clContent.getVisibility() != View.GONE;
                Context c = holder.binding.clContent.getContext();
                if(visible) {
                    ViewManager.collapse(holder.binding.clContent);
                    holder.binding.ivCollapse.setImageDrawable(c.getDrawable(R.drawable.ic_menu_down));
                }
                else{
                    ViewManager.expand(holder.binding.clContent);
                    holder.binding.ivCollapse.setImageDrawable(c.getDrawable(R.drawable.ic_menu_up));
                }
            }
        };

        holder.binding.ivCollapse.setOnClickListener(collapseOnClickListener);
        holder.binding.llGuideline.setOnClickListener(collapseOnClickListener);

        holder.binding.tvReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                SharedPreferences prefs = c.getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                boolean isLoggedIn = !prefs.getString("jwt", "").equals("");
                if(isLoggedIn) {
                    boolean visible = holder.binding.clReply.getVisibility() != View.GONE;
                    if (visible) {
                        ViewManager.collapse(holder.binding.clReply);
                    } else {
                        ViewManager.expand(holder.binding.clReply);
                    }
                }

            }
        });

        holder.binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewManager.collapse(holder.binding.clReply);
            }
        });

        holder.binding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                SharedPreferences prefs = c.getApplicationContext().getSharedPreferences("userData", Context.MODE_PRIVATE);
                boolean isLoggedIn = !prefs.getString("jwt", "").equals("");
                if(isLoggedIn) {
                    Comment newComment = new Comment();
                    newComment.setParentId(comments.get(position).getId());
                    newComment.setContent(holder.binding.etReply.getText().toString());
                    holder.binding.etReply.setText("");
                    listener.onSendReply(newComment);
                    ViewManager.collapse(holder.binding.clReply);
                }

            }
        });

        holder.binding.rvChildren.setLayoutManager(new LinearLayoutManager(holder.binding.rvChildren.getContext()));
        CommentAdapter adapter = new CommentAdapter(listener);
        holder.binding.rvChildren.setAdapter(adapter);
        adapter.setComments(comments.get(position).getChildren());
        holder.binding.rvChildren.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        if(comments == null)
            return 0;
        return comments.size();
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();

    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private RecipeCommentBinding binding;

        CommentViewHolder(@NonNull RecipeCommentBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

    }
}
