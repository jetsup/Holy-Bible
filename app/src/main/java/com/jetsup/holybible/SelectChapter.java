package com.jetsup.holybible;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.adapters.ChapterRecyclerViewAdapter;

import java.util.Objects;

public class SelectChapter extends AppCompatActivity {
    final String TAG = "MyTag_SC";
    String bookTitle, aliasName;
    String[] aliasNames = {};
    int[] chapters = {};
    RecyclerView chapterRecyclerView;
    ChapterRecyclerViewAdapter chapterViewAdapter;
    int bookIndex, thisBookChapters; // pass this to the RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chapter);
        chapterRecyclerView = findViewById(R.id.chapterRecyclerView);
        chapterRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        bookTitle = getIntent().getExtras().getString("BookTitle");
        Objects.requireNonNull(getSupportActionBar()).setTitle(bookTitle);
        bookIndex = getIntent().getExtras().getInt("BookIndex"); // 0 for Genesis...

        Log.w(TAG, "onCreate: About to fetch");
        aliasNames = getResources().getStringArray(R.array.filename_alias);
        Log.w(TAG, "onCreate: Fetched");
        chapters = getResources().getIntArray(R.array.book_chapters);
        Log.w(TAG, "onCreate: Fetched2");

        aliasName = aliasNames[bookIndex];
        thisBookChapters = chapters[bookIndex];

        // retrieve the number of chapters in the particular selected book and pass it to the viewHolder
        Log.w(TAG, "onCreate: Setting adapter");
        chapterViewAdapter = new ChapterRecyclerViewAdapter(this, bookIndex, bookTitle, thisBookChapters);
        Log.w(TAG, "onCreate: Setting done");

        chapterRecyclerView.setAdapter(chapterViewAdapter);
        Log.w(TAG, "onCreate: Activity set");
    }
}