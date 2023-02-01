package com.jetsup.holybible;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.adapters.VerseRecyclerViewAdapter;

import java.util.Objects;

public class ReadActivity extends AppCompatActivity {
    String bookTitle;
    String bookFile;
    VerseRecyclerViewAdapter verseAdapter;
    int bookIndex, readChapter;
    RecyclerView verseRecyclerView;

    String[] fileAliases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        verseRecyclerView = findViewById(R.id.verseRecyclerView);
        verseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (MainActivity.testament == 0) {
            fileAliases = getResources().getStringArray(R.array.old_filename_alias);
        } else if (MainActivity.testament == 1) {
            fileAliases = getResources().getStringArray(R.array.new_filename_alias);
        }

        bookTitle = getIntent().getExtras().getString("BookTitle");
        readChapter = getIntent().getExtras().getInt("Chapter");
        bookIndex = getIntent().getExtras().getInt("BookIndex");
        Objects.requireNonNull(getSupportActionBar()).setTitle(bookTitle + " " + readChapter);

        bookFile = fileAliases[bookIndex] + readChapter;
        verseAdapter = new VerseRecyclerViewAdapter(this, bookFile);
        verseRecyclerView.setAdapter(verseAdapter);
    }
}
