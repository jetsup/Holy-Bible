package com.jetsup.holybible;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.jetsup.holybible.adapters.BookTitleRecyclerViewAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static int testament = 0;
    public static String[] books = new String[]{};
    DrawerLayout activityMainDrawer;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    RecyclerView bookTitleRecyclerView;
    BookTitleRecyclerViewAdapter titleRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActionBar actionBar = getSupportActionBar();
//        Objects.requireNonNull(actionBar).setDisplayShowCustomEnabled(true);
//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.main_activity_search_view, null);
//        actionBar.setCustomView(view);
//        actionBar.startActionMode()

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
        } else if (item.getItemId() == R.id.navSearchBook) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    titleRecyclerViewAdapter.getFilter().filter(newText);
                    return false;
                }
            });
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_options_menu, menu);
        return true;
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
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
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
