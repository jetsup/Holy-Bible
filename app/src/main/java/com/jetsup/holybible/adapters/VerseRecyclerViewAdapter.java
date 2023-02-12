package com.jetsup.holybible.adapters;

import static com.jetsup.holybible.constants.FileHandlingConstants.SAVED_VERSE_PARENT_FOLDER;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsup.holybible.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;

public class VerseRecyclerViewAdapter extends RecyclerView.Adapter<VerseRecyclerViewAdapter.VerseRecyclerViewHolder>
        implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
    final String TAG = "MyTag";
    final Map<Integer, Integer> verseColor = new HashMap<>();
    final ClipboardManager clipboardManager;
    final List<Integer> lastDigits = new ArrayList<>();
    final List<String> verses = new ArrayList<>();
    final Context context;
    final List<String> versesList = new ArrayList<>();
    final InputStream inputStream;
    final TextToSpeech textToSpeech;
    private final int receivedChapter;
    private final String receivedBook;
    int verseIndex;
    ClipData clipData;
    String verse;
    int lastDigit = 0;
    int defaultColor;

    public VerseRecyclerViewAdapter(Context context, String fileName, TextToSpeech textToSpeech, String bookTitle, int readChapter) {
        this.context = context;
        this.textToSpeech = textToSpeech;
        this.receivedBook = bookTitle;
        this.receivedChapter = readChapter;
        try {
            loadSavedHighlightColors();
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultColor = context.getColor(R.color.teal_200);

        clipboardManager = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);

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

        for (int j = 0; j < versesList.size(); j++) {
            String verse = versesList.get(j);
            for (int i = 0; i < 5; i++) {
                try {
                    Integer.parseInt(String.valueOf(verse.charAt(i)));
                } catch (NumberFormatException e) {
                    lastDigit = i + 1;
                    lastDigits.add(lastDigit);
                    break;
                }
            }
        }
    }

    private void loadSavedHighlightColors() throws IOException {
        String childPath = SAVED_VERSE_PARENT_FOLDER + "/" + receivedBook + "/" + receivedBook + receivedChapter + ".txt";
        File readFile = new File(Objects.requireNonNull(context.getExternalCacheDir().getParentFile()).getPath(), childPath);

        if (readFile.exists()) {
            Log.w(TAG, "Loaded: " + readFile.getPath());
            try (FileInputStream fileInputStream = new FileInputStream(readFile)) {
                StringBuilder mapMaker = new StringBuilder();
                while (fileInputStream.available() > 0) {
                    int byteRead = fileInputStream.read();
                    mapMaker.append((char) byteRead);
                    if ((char) byteRead == '\n') {
                        String[] value = mapMaker.toString().trim().split(" ");
                        verseColor.put(Integer.parseInt(value[0]), Integer.parseInt(value[1]));
                        defaultColor = Integer.parseInt(value[1]);
                        mapMaker = new StringBuilder();
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public VerseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.verse_tile, parent, false);
        return new VerseRecyclerViewHolder(view);
    }

    private void readVerse(String verse) {
        textToSpeech.speak(verse, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull VerseRecyclerViewHolder holder, int position) {
        verse = versesList.get(position);
        verses.add(verse.substring(lastDigits.get(position)));
        Spannable spannable = new SpannableString(verse);
        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255, 78, 78)),
                0, lastDigits.get(position), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.verseText.setText(spannable, TextView.BufferType.SPANNABLE);

        holder.verseCardLayout.setOnLongClickListener(v -> {
            verseIndex = holder.getAdapterPosition();
            v.setOnCreateContextMenuListener(VerseRecyclerViewAdapter.this);
            v.showContextMenu(50, 10);
            return true;
        });
        Drawable bg;
        if (verseColor.containsKey(position)) {
            bg = verseHighlightDrawable(Objects.requireNonNull(verseColor.get(position)));
            bg.setTint(Objects.requireNonNull(verseColor.get(position)));

        } else {
            bg = ResourcesCompat.getDrawable(context.getResources(), R.drawable.circled_rectangle_outline, null);
        }
        holder.verseCardLayout.setBackground(bg);
    }

    public Drawable verseHighlightDrawable(int color) {
        RoundRectShape rectShape = new RoundRectShape(new float[]{25, 25, 25, 25, 25, 25, 25, 25}, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(rectShape);
        shapeDrawable.getPaint().setColor(color);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable.getPaint().setAntiAlias(true);
        shapeDrawable.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
        return shapeDrawable;
    }

    @Override
    public int getItemCount() {
        return versesList.size();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, v.getId(), Menu.NONE, "Copy").setOnMenuItemClickListener(this);
        menu.add(Menu.NONE, v.getId(), Menu.NONE, "Highlight").setOnMenuItemClickListener(item -> {
            new AmbilWarnaDialog(context, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                @Override
                public void onCancel(AmbilWarnaDialog dialog) {
                }

                @Override
                public void onOk(AmbilWarnaDialog dialog, int color) {
                    defaultColor = color;
                    Drawable drawable = verseHighlightDrawable(defaultColor);
                    Objects.requireNonNull(drawable).setTint(defaultColor);
                    v.setBackground(drawable);
                    v.postInvalidate();
                    verseColor.put(verseIndex, defaultColor);
                    try {
                        saveHighlightColor();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).show();
            return true;
        });
        menu.add(Menu.NONE, v.getId(), Menu.NONE, "Read Aloud").setOnMenuItemClickListener(this);
    }

    private void saveHighlightColor() throws IOException {
        String childFolder = SAVED_VERSE_PARENT_FOLDER + "/" + receivedBook;
        File folder = new File(Objects.requireNonNull(context.getExternalCacheDir().getParentFile()).getPath(),
                childFolder);

        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                Log.e(TAG, "Creating File Folder failed");
            }
        }
        File file = new File(folder + "/" + receivedBook + receivedChapter + ".txt");
        if (!file.createNewFile()) {
            Log.w(TAG, "File Exist");
        }
        // write the data in the Map instead
        Set<Integer> verseNumbersKeys = verseColor.keySet();
        for (int i : verseNumbersKeys) {
            byte[] dataToWrite = (i + " " + verseColor.get(i) + "\n").getBytes(); // Sample "Verse12 -7865876\n"
            try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
                fileOutputStream.write(dataToWrite);
            }
        }
    }

    @Override
    public boolean onMenuItemClick(@NonNull MenuItem item) {
        switch (item.getTitle().toString()) {
            case "Copy":
                clipData = ClipData.newPlainText("cpBibleVerse", verses.get(verseIndex));
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, "Verse Copied", Toast.LENGTH_SHORT).show();
                return true;
            case "Read Aloud":
                readVerse(verses.get(verseIndex));
                Toast.makeText(context, "Reading", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    static class VerseRecyclerViewHolder extends RecyclerView.ViewHolder {
        final RelativeLayout verseCardLayout;
        final TextView verseText;

        public VerseRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            verseText = itemView.findViewById(R.id.verseText);
            verseCardLayout = itemView.findViewById(R.id.verseCardLayout);
        }
    }
}
