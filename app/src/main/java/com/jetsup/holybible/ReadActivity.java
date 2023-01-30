package com.jetsup.holybible;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ReadActivity extends AppCompatActivity {
    final String TAG = "MyTag_RA";
    String bookTitle, alias, nextAlias;
    String line, chapterString;
    int bookIndex;

    String[] aliases = {"Ge", "Exo", "Lev", "Num", "Deu", "Josh", "Jdgs", "Ruth", "1Sm", "2Sm", "1Ki", "2Ki", "1Chr", "2Chr", "Ezra", "Neh", "Est", "Job", "Psa", "Prv", "Eccl", "SSol", "Isa", "Jer", "Lam", "Eze", "Dan", "Hos", "Joel", "Amos", "Obad", "Jonah", "Mic", "Nahum", "Hab", "Zep", "Hag", "Zec", "Mal", "Mat", "Mark", "Luke", "John", "Acts", "Rom", "1Cor", "2Cor", "Gal", "Eph", "Phi", "Col", "1Th", "2Th", "1Tim", "2Tim", "Titus", "Phmn", "Heb", "Jas", "1Pet", "2Pet", "1Jn", "2Jn", "3Jn", "Jude", "Rev"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        bookTitle = getIntent().getExtras().getString("BookTitle");
        bookIndex = getIntent().getExtras().getInt("BookIndex");
        alias = aliases[bookIndex];
        if (bookIndex < 65) {
            nextAlias = aliases[bookIndex + 1];
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(bookTitle);
//        Toast.makeText(this, "Book: '" + bookTitle + "' Book index: " + bookIndex + " Alias: '" + alias + "'", Toast.LENGTH_SHORT).show();
        try {
            readChapter();
        } catch (IOException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void readChapter() throws IOException {
        InputStream theBible = getResources().openRawResource(R.raw.bible_kjv);
        Log.w(TAG, "readChapter: Can read: " + theBible.available());

        while (true) {
            if (theBible.available() > 0) {
                line += String.valueOf((char) theBible.read());
                if (line.endsWith("\n")) {
                    if (line.startsWith(nextAlias)) {
                        break;
                    } else {
                        if (line.startsWith(alias)) {
                            chapterString += line;
                        } else {
                            line = "";
                        }
                    }
                }
            } else {
                Log.w(TAG, "readChapter: " + chapterString);
                break;
            }
        }

        Log.i(TAG, "readChapter: " + line);
    }
}