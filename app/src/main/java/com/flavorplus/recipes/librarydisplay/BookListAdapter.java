package com.flavorplus.recipes.librarydisplay;

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
import com.flavorplus.databinding.LibraryBookBinding;
import com.flavorplus.recipes.RecipeIcon;
import com.flavorplus.recipes.models.Book;

import java.util.ArrayList;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {
    private ArrayList<Book> books;
    private FragmentActivity activity;

    BookListAdapter(FragmentActivity activity){
        this.activity = activity;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LibraryBookBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.library_book, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        final Book book = books.get(position);
        holder.binding.setBook(book);
        ArrayList<FrameLayout> highlighFrames = new ArrayList<>();
        highlighFrames.add(holder.binding.flHighlight1);
        highlighFrames.add(holder.binding.flHighlight2);
        highlighFrames.add(holder.binding.flHighlight3);

        for(int i = 0; i < highlighFrames.size(); i++){
            if(books.get(position).getHighlight(i) != null)
                highlighFrames.get(i).addView(RecipeIcon.getView(highlighFrames.get(i), books.get(position).getHighlight(i)));
        }

        holder.binding.cvBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationViewModel nav = new ViewModelProvider(activity).get(NavigationViewModel.class);
                nav.setBookId(book.getId());
                nav.setScreen(NavigationViewModel.Screens.BOOKTEST);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(books == null)
            return 0;
        return books.size();
    }

    public void setBooks(ArrayList<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder{
        private LibraryBookBinding binding;

        BookViewHolder(@NonNull LibraryBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }




}
