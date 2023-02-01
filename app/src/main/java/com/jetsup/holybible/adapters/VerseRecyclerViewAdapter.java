package com.jetsup.holybible.adapters;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class VerseRecyclerViewAdapter extends RecyclerView.Adapter<VerseRecyclerViewAdapter.VerseRecyclerViewHolder> {
    static List<String> verses = new ArrayList<>();
    static int verseIndex;
    final String TAG = "MyTag_VRVA";
    Context context;
    List<String> versesList = new ArrayList<>();
    InputStream inputStream;

    public VerseRecyclerViewAdapter(Context context, String fileName) {
        this.context = context;
        @SuppressLint("DiscouragedApi")
        int identifier = this.context.getResources().getIdentifier(fileName, "raw", this.context.getPackageName());
        inputStream = this.context.getResources().openRawResource(identifier);

        StringBuilder buffer = new StringBuilder();
        try {
            while (inputStream.available() > 0) {
                int byteRead = inputStream.read();
                buffer.append((char) byteRead);
                if (byteRead == 10) {
                    buffer.replace(0, buffer.indexOf(":") + 1, "");
                    versesList.add(buffer.toString().trim());
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
        String verse = versesList.get(position);
        int lastDigit = 0;
        boolean digitFound = true;
        // get verse [number] index
        while (digitFound) {
            for (int i = 0; i < 5; i++) {
                try {
                    Integer.parseInt(String.valueOf(verse.charAt(i)));
                } catch (NumberFormatException e) {
                    lastDigit = i + 1;
                    digitFound = false;
                    verses.add(new StringBuilder().append(versesList.get(position)).substring(i + 1));
                    break;
                }
            }
        }
        Spannable spannable = new SpannableString(verse);
        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 78, 78)), 0, lastDigit, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.verseText.setText(spannable, TextView.BufferType.SPANNABLE);
        holder.verseCardLayout.setOnLongClickListener(v -> {
            verseIndex = holder.getAdapterPosition();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return versesList.size();
    }

    static class VerseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextToSpeech textToSpeech;
        RelativeLayout verseCardLayout;
        ClipboardManager clipboardManager;
        ClipData clipData;
        boolean canSpeak;
        TextView verseText;

        public VerseRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            verseText = itemView.findViewById(R.id.verseText);
            verseCardLayout = itemView.findViewById(R.id.verseCardLayout);
            verseCardLayout.setOnCreateContextMenuListener(this);

            clipboardManager = (ClipboardManager) itemView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);

            textToSpeech = new TextToSpeech(itemView.getContext(), status -> {
                if (status != TextToSpeech.ERROR) {
                    canSpeak = true;
                }
            });
            if (canSpeak) {
                textToSpeech.setSpeechRate(0.01f);
                textToSpeech.setPitch(1.0f);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, v.getId(), Menu.NONE, "Copy").setOnMenuItemClickListener(this);
            menu.add(Menu.NONE, v.getId(), Menu.NONE, "Highlight").setOnMenuItemClickListener(this);
            menu.add(Menu.NONE, v.getId(), Menu.NONE, "Read Aloud").setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(@NonNull MenuItem item) {
            if (item.getTitle().equals("Copy")) {
                clipData = ClipData.newPlainText("CopiedVerse", verses.get(verseIndex));
                clipboardManager.setPrimaryClip(clipData);
                return true;
            } else if (item.getTitle().equals("Highlight")) {
                Toast.makeText(itemView.getContext(), "Highlight", Toast.LENGTH_SHORT).show();
                return true;
            } else if (item.getTitle().equals("Read Aloud")) {
                readVerse(verses.get(verseIndex));
                return true;
            } else {
                return false;
            }
        }

        private void readVerse(String verse) {
            textToSpeech.speak(verse, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
}
