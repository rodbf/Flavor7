package com.flavorplus.recipes.recipedisplay.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.flavorplus.FlavorApplication;
import com.flavorplus.R;
import com.flavorplus.recipes.bookmarkdialog.BookmarkDialogFragment;
import com.flavorplus.databinding.RecipeFragmentBinding;
import com.flavorplus.recipes.models.VoteState;
import com.flavorplus.recipes.recipedisplay.RecipeDisplayViewModel;
import com.flavorplus.recipes.models.Recipe;
import com.flavorplus.recipes.recipedisplay.comments.Comment;
import com.flavorplus.recipes.recipedisplay.comments.CommentAdapter;
import com.flavorplus.recipes.recipedisplay.comments.CommentViewModel;
import com.flavorplus.util.ViewManager;

import java.util.ArrayList;
import java.util.Objects;


public class RecipeDisplayFragment extends Fragment implements View.OnClickListener, BookmarkDialogFragment.BookmarkDialogListener, CommentAdapter.CommentListener {

    private RecipeDisplayViewModel viewModel;
    private ListItemAdapter ingredientsAdapter, stepsAdapter;
    private int recipeId;
    private CommentViewModel commentViewModel;
    private Observer<ArrayList<Comment>> commentObserver;

    private RecipeFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecipeFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(RecipeDisplayViewModel.class);
        commentViewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        binding.clIngredients.findViewById(R.id.tvTitle).setOnClickListener(this);
        ((TextView)binding.clIngredients.findViewById(R.id.tvTitle)).setText("Ingredientes");
        RecyclerView ingredientsView = binding.clIngredients.findViewById(R.id.rvParentList);
        ingredientsView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ingredientsView.setHasFixedSize(true);
        ingredientsAdapter = new ListItemAdapter();
        ingredientsView.setAdapter(ingredientsAdapter);

        binding.clSteps.findViewById(R.id.tvTitle).setOnClickListener(this);
        ((TextView)binding.clSteps.findViewById(R.id.tvTitle)).setText("Modo de Preparo");
        RecyclerView stepsView = binding.clSteps.findViewById(R.id.rvParentList);
        stepsView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        stepsView.setHasFixedSize(true);
        stepsAdapter = new ListItemAdapter();
        stepsView.setAdapter(stepsAdapter);

        viewModel.recipeLiveData.observe(this.requireActivity(), new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                Log.d("debugging", "recipeLiveData onChanged");
                ingredientsAdapter.setParentList(recipe.getIngredients());
                stepsAdapter.setParentList(recipe.getSteps());
                ImageLoader imageLoader = FlavorApplication.getImgController().getImageLoader();
                imageLoader.get(viewModel.getImgUrl(), ImageLoader.getImageListener(binding.nivRecipeIcon, R.drawable.ic_recipe_placeholder, R.drawable.ic_recipe_placeholder));
                binding.nivRecipeIcon.setImageUrl(viewModel.getImgUrl(), imageLoader);
                if(recipe.isFavorite())
                    binding.btSave.setImageResource(R.drawable.ic_heart_full);
                else
                    binding.btSave.setImageResource(R.drawable.ic_heart_empty);
                commentViewModel.setRecipeId(recipe.getId());
                }
        });

        viewModel.voteState.observe(this.requireActivity(), new Observer<VoteState>() {
            @Override
            public void onChanged(VoteState voteState) {

                if(voteState.getVote() == 1){
                    binding.btUpvote.setImageResource(R.drawable.ic_like_full);
                    binding.btDownvote.setImageResource(R.drawable.ic_dislike_empty);
                }
                else if(voteState.getVote() == -1){
                    binding.btUpvote.setImageResource(R.drawable.ic_like_empty);
                    binding.btDownvote.setImageResource(R.drawable.ic_dislike_full);
                }
                else {
                    binding.btUpvote.setImageResource(R.drawable.ic_like_empty);
                    binding.btDownvote.setImageResource(R.drawable.ic_dislike_empty);
                }
            }
        });

        recipeId = getArguments().getInt("recipeId");
        viewModel.getRecipeById(recipeId);
        binding.btSave.setOnClickListener(this);


        binding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        final CommentAdapter commentAdapter = new CommentAdapter(this);
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setNestedScrollingEnabled(false);
        commentObserver = new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                commentAdapter.setComments(comments);
            }
        };

        binding.tvComments.setOnClickListener(this);
        binding.btConfirm.setOnClickListener(this);
        binding.btUpvote.setOnClickListener(this);
        binding.btDownvote.setOnClickListener(this);



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == binding.clIngredients.findViewById(R.id.tvTitle).getId() ||
                v.getId() == binding.clSteps.findViewById(R.id.tvTitle).getId()){
            changeRecyclerVisibility(v);
        }

        else if(v.getId() == binding.btSave.getId()){
            if(viewModel.isLoggedIn()) {
                if (!Objects.requireNonNull(viewModel.recipeLiveData.getValue()).isFavorite())
                    showBookmarkDialog();
                else
                    viewModel.removeFavorite();
            }
        }

        else if(v.getId() == binding.tvComments.getId()){
            if(binding.clComment.getVisibility() != View.GONE){
                ViewManager.collapse(binding.clComment);
                binding.tvComments.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_down, 0);
            }
            else{
                ViewManager.expand(binding.clComment);
                binding.tvComments.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_up, 0);
            }
        }

        if(v.getId() == binding.btConfirm.getId()){
            if(viewModel.isLoggedIn()) {
                Comment newComment = new Comment();
                newComment.setParentId(0);
                newComment.setContent(binding.etReply.getText().toString().trim());
                binding.etReply.setText("");
                commentViewModel.createComment(newComment);
            }
        }

        if(v.getId() == binding.btUpvote.getId()){
            if(viewModel.voteState.getValue().getVote() == 1)
                viewModel.vote(recipeId, 0);
            else
                viewModel.vote(recipeId, 1);

        }

        if(v.getId() == binding.btDownvote.getId()){
            if(viewModel.voteState.getValue().getVote() == -1)
                viewModel.vote(recipeId, 0);
            viewModel.vote(recipeId, -1);
        }
    }

    private void changeRecyclerVisibility(View v){
        if(((View)v.getParent()).findViewById(R.id.rvParentList).getVisibility() == View.GONE) {
            expandRecycler(v);
        }
        else {
            collapseRecycler(v);
        }
    }

    private void collapseRecycler(View v){
        RecyclerView rv = ((View)v.getParent()).findViewById(R.id.rvParentList);
        TextView tv = ((View)v.getParent()).findViewById(R.id.tvTitle);
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_down, 0);
        ViewManager.collapse(rv);
    }

    private void expandRecycler(View v){
        RecyclerView rv = ((View)v.getParent()).findViewById(R.id.rvParentList);
        TextView tv = ((View)v.getParent()).findViewById(R.id.tvTitle);
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_menu_up, 0);
        ViewManager.expand(rv);
    }

    private void showBookmarkDialog(){
        FragmentManager fm = getParentFragmentManager();
        BookmarkDialogFragment bookmarkDialogFragment = new BookmarkDialogFragment();
        Bundle args = new Bundle();
        args.putInt("recipeId", recipeId);
        bookmarkDialogFragment.setArguments(args);
        bookmarkDialogFragment.setTargetFragment(RecipeDisplayFragment.this, 0);
        bookmarkDialogFragment.show(fm, "dialog_frag");
    }

    @Override
    public void onBookmarkAdded(boolean favorite) {
        if(favorite){
            Toast.makeText(requireContext(), "favorite added", Toast.LENGTH_SHORT).show();
            viewModel.setFavorite(true);
        }
        else
            Toast.makeText(requireContext(), "bookmark added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendReply(Comment reply) {
        commentViewModel.createComment(reply);
    }

    @Override
    public void onDestroy() {
        commentViewModel.comments.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        commentViewModel.comments.observe(this, commentObserver);
    }
}
