package com.flavorplus.recipes.bookmarkdialog;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.flavorplus.R;
import com.flavorplus.recipes.models.Book;
import com.flavorplus.databinding.BookmarkDialogFragmentBinding;
import com.flavorplus.util.ViewManager;

import java.util.ArrayList;

public class BookmarkDialogFragment extends DialogFragment implements Spinner.OnItemSelectedListener, TextView.OnEditorActionListener, View.OnClickListener {

    public interface BookmarkDialogListener{
        void onBookmarkAdded(boolean favorite);
    }

    private BookmarkDialogFragmentBinding binding;
    private BookmarkDialogViewModel viewModel;
    private BookmarkDialogListener listener;
    private Observer<ArrayList<Book>> bookListObserver;
    private Observer<String> errorObserver;
    private Observer<Boolean> favoriteObserver;
    private Observer<Integer> selectedBookObserver;

    private void selectBook(){
        ArrayList<Book> list = new ArrayList<>();
        for(int i = 0; i < binding.spBooks.getCount(); i++)
            list.add((Book)binding.spBooks.getItemAtPosition(i));
        viewModel.selectBook(list, binding.spBooks.getSelectedItemPosition(), binding.etNewBook.getText().toString());
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == binding.btConfirm.getId())
            selectBook();
        else if(view.getId() == binding.tvCancel.getId())
            dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(pos == 1) {
            binding.etNewBook.setVisibility(View.VISIBLE);
            ViewManager.showKeyboard(binding.etNewBook);
        }
        else
            binding.etNewBook.setVisibility(View.GONE);
        viewModel.setSelectedBook(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            selectBook();
            return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = BookmarkDialogFragmentBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(BookmarkDialogViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.spBooks.setOnItemSelectedListener(this);

        viewModel.setRecipeId(getArguments().getInt("recipeId"));
        viewModel.getUserBooks();

        binding.btConfirm.setOnClickListener(this);
        binding.tvCancel.setOnClickListener(this);
        binding.etNewBook.setOnEditorActionListener(this);

        listener = (BookmarkDialogListener) getTargetFragment();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bookListObserver = new Observer<ArrayList<Book>>() {
            @Override
            public void onChanged(ArrayList<Book> books) {
                ArrayAdapter<Book> bookSpinnerAdapter = new ArrayAdapter<>(requireContext(), R.layout.generic_spinner_item, books);
                binding.spBooks.setAdapter(bookSpinnerAdapter);
            }
        };

        errorObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show();
            }
        };

        favoriteObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean favorite) {
                listener.onBookmarkAdded(favorite);
                dismiss();
            }
        };

        selectedBookObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if(binding.spBooks.getSelectedItemPosition() != integer)
                    binding.spBooks.setSelection(integer);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.books.observe(requireActivity(), bookListObserver);
        viewModel.err.observe(requireActivity(), errorObserver);
        viewModel.favorited.observe(requireActivity(), favoriteObserver);
        viewModel.selectedBook.observe(requireActivity(), selectedBookObserver);
    }

    @Override
    public void onDestroy() {
        viewModel.books.removeObservers(this);
        viewModel.err.removeObservers(this);
        viewModel.favorited.removeObservers(this);
        viewModel.selectedBook.removeObservers(this);
        super.onDestroy();
    }
}
