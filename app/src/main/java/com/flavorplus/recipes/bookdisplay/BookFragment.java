package com.flavorplus.recipes.bookdisplay;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;


import com.flavorplus.NavigationViewModel;
import com.flavorplus.R;
import com.flavorplus.databinding.BookFragmentBinding;
import com.flavorplus.recipes.models.Book;
import com.flavorplus.util.ViewManager;

public class BookFragment extends Fragment implements View.OnClickListener, RecipeListAdapter.RecipeAdapterListener {

    private BookViewModel viewModel;
    private RecipeListAdapter recipesAdapter;
    private Observer<Book> bookObserver;

    private BookFragmentBinding binding;
    private NavigationViewModel navigationViewModel;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BookFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);
        navigationViewModel = new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        viewModel.setBook(navigationViewModel.getBookId());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getActivity());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        prefs = requireActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);

        binding.rvLibrary.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.rvLibrary.setHasFixedSize(false);
        recipesAdapter = new RecipeListAdapter(requireActivity(), this);
        binding.rvLibrary.setAdapter(recipesAdapter);
        binding.rvLibrary.setNestedScrollingEnabled(false);

        bookObserver = new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                recipesAdapter.setRecipes(book.getRecipes());
                Log.d("debugging", "user_id: "+prefs.getInt("user_id", -1)+" book_id: "+book.getId());
                if(prefs.getInt("user_id", -1) != book.getOwnerId() || book.isDefaultBook()){
                    binding.ivEditBook.setVisibility(View.VISIBLE);
                    binding.ivEditBook.setVisibility(View.GONE);
                }
                else{
                    binding.ivEditBook.setVisibility(View.GONE);
                    binding.ivEditBook.setVisibility(View.VISIBLE);
                    binding.ivEditBook.setOnClickListener(BookFragment.this);
                }
            }
        };

        binding.ivEditBook.setOnClickListener(this);
        binding.tvDelete.setOnClickListener(this);
        binding.btEditBookConfirm.setOnClickListener(this);
        binding.etNewName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    changeBookName();
                return true;
            }
                return true;
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == binding.ivEditBook.getId()){
            if (binding.cvEdit.getVisibility() == View.GONE)
                ViewManager.expand(binding.cvEdit);
            else
                ViewManager.collapse(binding.cvEdit);
        }
        else if(v.getId() == binding.tvDelete.getId()){
            new AlertDialog.Builder(requireContext())
                    .setTitle("Deletar livro?")
                    .setMessage("Deseja deletar o livro \""+ binding.etTitle.getText().toString() +"\"?")
                    .setIcon(R.drawable.ic_delete_outline)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            viewModel.deleteBook();
                            navigationViewModel.setScreen(NavigationViewModel.Screens.LIBRARY, false);
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else if(v.getId() == binding.btEditBookConfirm.getId()){
            changeBookName();
        }
    }

    @Override
    public void onDestroy() {
        viewModel.book.removeObservers(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.book.observe(this, bookObserver);
    }

    @Override
    public void onRemoveRecipe(int id) {
        viewModel.removeRecipe(id);
    }

    private void changeBookName(){
        viewModel.setBookName(binding.etNewName.getText().toString());
        ViewManager.collapse(binding.cvEdit);
        ViewManager.closeKeyboard(binding.cvEdit);
    }
}
