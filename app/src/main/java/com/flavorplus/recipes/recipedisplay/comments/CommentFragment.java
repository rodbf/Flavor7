package com.flavorplus.recipes.recipedisplay.comments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flavorplus.R;
import com.flavorplus.databinding.CommentFragmentBinding;

import java.util.ArrayList;

public class CommentFragment extends Fragment implements CommentAdapter.CommentListener {

    private CommentViewModel viewModel;
    private CommentFragmentBinding binding;
    private Observer<ArrayList<Comment>> commentObserver;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CommentFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        viewModel.setRecipeId(1);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(requireContext()));
        final CommentAdapter adapter = new CommentAdapter(this);
        binding.rvComments.setAdapter(adapter);
        binding.rvComments.setNestedScrollingEnabled(false);
        commentObserver = new Observer<ArrayList<Comment>>() {
            @Override
            public void onChanged(ArrayList<Comment> comments) {
                adapter.setComments(comments);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        viewModel.comments.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.comments.observe(this, commentObserver);
    }

    @Override
    public void onSendReply(Comment reply) {
        viewModel.createComment(reply);
    }
}
