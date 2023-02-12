package com.jetsup.holybible.adapters;

import static com.jetsup.holybible.constants.FileHandlingConstants.SAVED_VERSE_PARENT_FOLDER;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.R;
import com.jetsup.holybible.ReadActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChapterRecyclerViewAdapter extends RecyclerView.Adapter<ChapterRecyclerViewAdapter.MyChapterViewHolder> {
    final Context context;
    final List<Integer> chapters;
    final String bookTitle;
    final int bookIndex;
    final String TAG = "MyTag";
    Set<Integer> chaptersHighlight = new HashSet<>();

    public ChapterRecyclerViewAdapter(Context context, int bookIndex, String bookTitle, int chaptersToRender) {
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookIndex = bookIndex;
        chapters = new ArrayList<>();
        for (int i = 0; i < chaptersToRender; i++) {
            chapters.add(i);
        }
        determineHighlights();
    }

    private void determineHighlights() {
        String childFolder = SAVED_VERSE_PARENT_FOLDER + "/" + bookTitle;
        File folder;
        for (int i = 0; i < chapters.size(); i++) {
            folder = new File(Objects.requireNonNull(context.getExternalCacheDir().getParentFile()).getPath(),
                    childFolder + "/" + bookTitle + (i + 1) + ".txt");
            if (folder.exists()) {
                chaptersHighlight.add(i);
            }
        }
        Log.w(TAG, "Highlights: " + chaptersHighlight);
    }

    @NonNull
    @Override
    public MyChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chapter_select_card, parent, false);
        return new MyChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChapterViewHolder holder, int position) {
        holder.chapterNumber.setText(String.valueOf(chapters.get(position) + 1));
        holder.chapterCard.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("Chapter", holder.getAdapterPosition() + 1); // +1 for the zero index
            intent.putExtra("BookModel", bookTitle);
            intent.putExtra("BookIndex", bookIndex);
            intent.setClass(context, ReadActivity.class);
            context.startActivity(intent);
        });
        if (chaptersHighlight.contains(position)) {
            holder.highlightIcon.setVisibility(View.VISIBLE);
        } else {
            holder.highlightIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class MyChapterViewHolder extends RecyclerView.ViewHolder {
        final CardView chapterCard;
        final TextView chapterNumber;
        ImageView highlightIcon;

        public MyChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterCard = itemView.findViewById(R.id.chapterCard);
            chapterNumber = itemView.findViewById(R.id.chapterNumber);
            highlightIcon = itemView.findViewById(R.id.highlightIcon);
        }
    }
}
