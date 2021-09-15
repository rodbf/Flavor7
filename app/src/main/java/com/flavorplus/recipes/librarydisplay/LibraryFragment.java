package com.flavorplus.recipes.librarydisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flavorplus.databinding.LibraryFragmentBinding;
import com.flavorplus.recipes.models.Book;

import java.util.ArrayList;

public class LibraryFragment extends Fragment implements View.OnClickListener{

    private LibraryViewModel viewModel;
    private BookListAdapter booksAdapter;
    private Observer<ArrayList<Book>> libraryObserver;

    private LibraryFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LibraryFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(LibraryViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        binding.rvLibrary.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLibrary.setHasFixedSize(false);
        booksAdapter = new BookListAdapter(requireActivity());
        binding.rvLibrary.setAdapter(booksAdapter);
        binding.rvLibrary.setNestedScrollingEnabled(false);

        libraryObserver = new Observer<ArrayList<Book>>() {
            @Override
            public void onChanged(ArrayList<Book> books) {
                booksAdapter.setBooks(books);
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        viewModel.books.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.books.observe(this, libraryObserver);
    }
}
