package com.jetsup.holybible;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.adapters.ChapterRecyclerViewAdapter;

import java.util.Objects;

public class SelectChapter extends AppCompatActivity {
    final String TAG = "MyTag";
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

        bookTitle = getIntent().getExtras().getString("BookModel");
        Objects.requireNonNull(getSupportActionBar()).setTitle(bookTitle);
        bookIndex = getIntent().getExtras().getInt("BookIndex"); // 0 for Genesis...

        if (MainActivity.testament == 0) {
            aliasNames = getResources().getStringArray(R.array.old_filename_alias);
            chapters = getResources().getIntArray(R.array.old_book_chapters);
        } else if (MainActivity.testament == 1) {
            aliasNames = getResources().getStringArray(R.array.new_filename_alias);
            chapters = getResources().getIntArray(R.array.new_book_chapters);
        }

        aliasName = aliasNames[bookIndex];
        thisBookChapters = chapters[bookIndex];

        Log.w(TAG, "BookIndex: " + bookIndex + " <Title> " + bookTitle + " <Chapters> " + thisBookChapters);
        chapterViewAdapter = new ChapterRecyclerViewAdapter(this, bookIndex, bookTitle, thisBookChapters);

        chapterRecyclerView.setAdapter(chapterViewAdapter);
    }
}
