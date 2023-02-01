package com.jetsup.holybible;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.jetsup.holybible.adapters.BookTitleRecyclerViewAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int testament = 0;
    DrawerLayout activityMainDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView bookTitleRecyclerView;
    BookTitleRecyclerViewAdapter titleRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainDrawer = findViewById(R.id.activityMainDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, activityMainDrawer, R.string.nav_open, R.string.nav_close);
        activityMainDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        bookTitleRecyclerView = findViewById(R.id.bookTitleRecyclerView);
        bookTitleRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // important
        titleRecyclerViewAdapter = new BookTitleRecyclerViewAdapter(MainActivity.this);
        bookTitleRecyclerView.setAdapter(titleRecyclerViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) { // listen on ham button in the action bar
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navOldTestament) {
            if (MainActivity.testament != 0) {
                MainActivity.testament = 0;
                titleRecyclerViewAdapter.loadData(MainActivity.this);
                titleRecyclerViewAdapter.notifyDataSetChanged();
                bookTitleRecyclerView.scrollToPosition(0);
            }
            activityMainDrawer.closeDrawer(GravityCompat.START, true);
            return true;
        } else if (item.getItemId() == R.id.navNewTestament) {
            if (MainActivity.testament != 1) {
                MainActivity.testament = 1;
                titleRecyclerViewAdapter.loadData(MainActivity.this);
                titleRecyclerViewAdapter.notifyDataSetChanged();
                bookTitleRecyclerView.scrollToPosition(0);
            }
            activityMainDrawer.closeDrawer(GravityCompat.START, true);
            return true;
        } else if (item.getItemId() == R.id.navSettings) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            activityMainDrawer.closeDrawer(GravityCompat.START, true);
            return true;
        } else if (item.getItemId() == R.id.navAbout) {
            Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
            activityMainDrawer.closeDrawer(GravityCompat.START, true);
            return true;
        } else {
            return false;
        }
    }
}
