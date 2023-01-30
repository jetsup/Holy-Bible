package com.jetsup.holybible;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.adapters.BookTitleRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView bookTitleRecyclerView;
    BookTitleRecyclerViewAdapter titleRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookTitleRecyclerView = findViewById(R.id.bookTitleRecyclerView);
        titleRecyclerViewAdapter = new BookTitleRecyclerViewAdapter(MainActivity.this);
        bookTitleRecyclerView.setAdapter(titleRecyclerViewAdapter);
    }
}