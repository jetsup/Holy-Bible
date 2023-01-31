package com.jetsup.holybible.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VerseRecyclerViewAdapter extends RecyclerView.Adapter<VerseRecyclerViewAdapter.VerseRecyclerViewHolder> {
    final String TAG = "MyTag_VRVA";
    Context context;
    List<String> verses = new ArrayList<>();
    InputStream inputStream;

    public VerseRecyclerViewAdapter(Context context, String fileName) {
        this.context = context;
        @SuppressLint("DiscouragedApi")
        int identifier = this.context.getResources().getIdentifier(fileName, "raw", this.context.getPackageName());
//        Log.w(TAG, "VerseRecyclerViewAdapter: Identifier: " + identifier + " <-> " + R.raw.ge1);
        inputStream = this.context.getResources().openRawResource(identifier);
        Log.w(TAG, "MarkSupported: " + inputStream.markSupported());

        StringBuilder buffer = new StringBuilder();
        try {
            while (inputStream.available() > 0) {
                int byteRead = inputStream.read();
                buffer.append((char) byteRead);
                if (byteRead == 10) {
                    buffer.replace(0, buffer.indexOf(":") + 1, "");
                    verses.add(buffer.toString());
                    buffer = new StringBuilder();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public VerseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verse_tile, parent, false);
        return new VerseRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VerseRecyclerViewHolder holder, int position) {
        holder.verseText.setText(verses.get(position));
    }

    @Override
    public int getItemCount() {
        return verses.size();
    }

    static class VerseRecyclerViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout verseCardLayout;
        TextView verseText;

        public VerseRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            verseText = itemView.findViewById(R.id.verseText);
            verseCardLayout = itemView.findViewById(R.id.verseCardLayout);
        }
    }
}