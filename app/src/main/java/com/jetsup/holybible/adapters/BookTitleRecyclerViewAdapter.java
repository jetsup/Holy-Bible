package com.jetsup.holybible.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDivider;
import com.jetsup.holybible.R;
import com.jetsup.holybible.ReadActivity;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookTitleRecyclerViewAdapter extends RecyclerView.Adapter<BookTitleRecyclerViewAdapter.MyBookTitleVH> {
    final String TAG = "MyTag_BtRVA";
    List<String> bookTitles;
    List<String> bookDescription;
    Context context;

    public BookTitleRecyclerViewAdapter(Context context) {
        this.context = context;
        bookTitles = Arrays.asList(context.getResources().getStringArray(R.array.book_titles));
        bookDescription = Arrays.asList(context.getResources().getStringArray(R.array.book_description));
        Log.w(TAG, "Books: " + bookTitles.size());
        Log.w(TAG, "BookDescription: " + bookDescription.size());
    }

    @NonNull
    @Override
    public MyBookTitleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_title_tile, parent, false);
        return new MyBookTitleVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookTitleVH holder, int position) {
        holder.bookTitle.setText(bookTitles.get(position));
        holder.bookDescription.setText(bookDescription.get(position));
        if (position == bookTitles.size() - 1) {
            holder.bookDivider.setVisibility(View.GONE);
        }
        holder.bookTitleTile.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("BookTitle", bookTitles.get(position));
            intent.putExtra("BookIndex", position);
            intent.setClass(context, ReadActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookTitles.size();
    }

    static class MyBookTitleVH extends RecyclerView.ViewHolder {
        CircleImageView smallLogo;
        TextView bookTitle, bookDescription;
        MaterialDivider bookDivider;
        RelativeLayout bookTitleTile;

        public MyBookTitleVH(@NonNull View itemView) {
            super(itemView);
            smallLogo = itemView.findViewById(R.id.smallLogo);
            bookTitle = itemView.findViewById(R.id.bookTitle);
            bookDescription = itemView.findViewById(R.id.bookDescription);
            bookDivider = itemView.findViewById(R.id.bookDivider);
            bookTitleTile = itemView.findViewById(R.id.bookTitleTile);
        }
    }
}