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

    String[] fileAliases;// = {"Ge", "Exo", "Lev", "Num", "Deu", "Josh", "Jdgs", "Ruth", "1Sm", "2Sm", "1Ki", "2Ki", "1Chr", "2Chr", "Ezra", "Neh", "Est", "Job", "Psa", "Prv", "Eccl", "SSol", "Isa", "Jer", "Lam", "Eze", "Dan", "Hos", "Joel", "Amos", "Obad", "Jonah", "Mic", "Nahum", "Hab", "Zep", "Hag", "Zec", "Mal", "Mat", "Mark", "Luke", "John", "Acts", "Rom", "1Cor", "2Cor", "Gal", "Eph", "Phi", "Col", "1Th", "2Th", "1Tim", "2Tim", "Titus", "Phmn", "Heb", "Jas", "1Pet", "2Pet", "1Jn", "2Jn", "3Jn", "Jude", "Rev"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        verseRecyclerView = findViewById(R.id.verseRecyclerView);
        verseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileAliases = getResources().getStringArray(R.array.filename_alias);

        bookTitle = getIntent().getExtras().getString("BookTitle");
        readChapter = getIntent().getExtras().getInt("Chapter");
        bookIndex = getIntent().getExtras().getInt("BookIndex");
        Objects.requireNonNull(getSupportActionBar()).setTitle(bookTitle + " " + readChapter);

        bookFile = fileAliases[bookIndex] + readChapter;
        verseAdapter = new VerseRecyclerViewAdapter(this, bookFile);
        verseRecyclerView.setAdapter(verseAdapter);
    }
}