package com.jetsup.holybible.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.MainActivity;
import com.jetsup.holybible.R;
import com.jetsup.holybible.SelectChapter;
import com.jetsup.holybible.models.BookModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookTitleRecyclerViewAdapter extends RecyclerView.Adapter<BookTitleRecyclerViewAdapter.MyBookTitleVH> implements Filterable {
    final Context context;
    final String TAG = "MyTag";
    List<String> bookTitles;
    ArrayList<BookModel> books;
    ArrayList<BookModel> booksSearchable;
    private final Filter bookSearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BookModel> probableSearch = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                probableSearch.addAll(booksSearchable);
            } else {
                String searchPattern = constraint.toString().toLowerCase().trim();
                for (BookModel book : booksSearchable) {
                    if (book.getBookTitle().toLowerCase().startsWith(searchPattern)) {
                        probableSearch.add(book);
                    }
                }
            }
            FilterResults searchResult = new FilterResults();
            searchResult.values = probableSearch;

            return searchResult;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            //noinspection unchecked
            books.addAll((Collection<? extends BookModel>) results.values);
            notifyDataSetChanged();
        }
    };
    List<String> bookDescription;

    public BookTitleRecyclerViewAdapter(Context context) {
        this.context = context;
        loadData(context);
    }

    public void loadData(Context context) {
        books = new ArrayList<>();
        if (MainActivity.testament == 0) {
            bookTitles = Arrays.asList(context.getResources().getStringArray(R.array.old_testament_books));
            bookDescription = Arrays.asList(context.getResources().getStringArray(R.array.old_book_description));

            for (int i = 0; i < bookTitles.size(); i++) {
                books.add(new BookModel(bookTitles.get(i), bookDescription.get(i), R.drawable.baseline_menu_book_24));
            }
            booksSearchable = new ArrayList<>(books);
//            MainActivity.books = bookTitles.toArray(new String[0]);
        } else if (MainActivity.testament == 1) {
            bookTitles = Arrays.asList(context.getResources().getStringArray(R.array.new_testament_books));
            bookDescription = Arrays.asList(context.getResources().getStringArray(R.array.new_book_description));

            for (int i = 0; i < bookTitles.size(); i++) {
                books.add(new BookModel(bookTitles.get(i), bookDescription.get(i), R.drawable.baseline_menu_book_24));
            }
            booksSearchable = new ArrayList<>(books);
        }
    }

    @NonNull
    @Override
    public MyBookTitleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_title_tile, parent, false);
        return new MyBookTitleVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookTitleVH holder, int position) {
        holder.bookTitle.setText(books.get(position).getBookTitle());
        holder.bookDescription.setText(books.get(position).getBookDescription());
        holder.smallLogo.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),
                books.get(position).getBookCoverPic(), null));
        holder.bookTitleTile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("BookModel", books.get(position).getBookTitle());
            intent.putExtra("BookIndex", bookTitles.indexOf(books.get(position).getBookTitle()));
            intent.setClass(context, SelectChapter.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public Filter getFilter() {
        return bookSearchFilter;
    }

    static class MyBookTitleVH extends RecyclerView.ViewHolder {
        final CircleImageView smallLogo;
        final TextView bookTitle;
        final TextView bookDescription;
        final CardView bookTitleTile;

        public MyBookTitleVH(@NonNull View itemView) {
            super(itemView);
            smallLogo = itemView.findViewById(R.id.smallLogo);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookDescription = itemView.findViewById(R.id.bookDescription);
//            bookDivider = itemView.findViewById(R.id.bookDivider);
            bookTitleTile = itemView.findViewById(R.id.bookTitleTile);
        }
    }
}
