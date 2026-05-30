package com.example.recipespringandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.recipespringandroid.R;
import com.example.recipespringandroid.fragments.CreateRecipeFragment;
import com.example.recipespringandroid.fragments.FavoritesFragment;
import com.example.recipespringandroid.fragments.HomeFragment;
import com.example.recipespringandroid.fragments.SearchFragment;
import com.example.recipespringandroid.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        androidx.appcompat.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        setupBottomNavigation();

        loadFragment(new HomeFragment());
    }

    private void setupBottomNavigation() {

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_home) {
                loadFragment(new HomeFragment());
                return true;
            }

            if (id == R.id.menu_search) {
                loadFragment(new SearchFragment());
                return true;
            }

            if (id == R.id.menu_favorites) {
                loadFragment(new FavoritesFragment());
                return true;
            }

            if (id == R.id.menu_create) {
                loadFragment(new CreateRecipeFragment());
                return true;
            }

            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_profile) {

            if (sessionManager.isLoggedIn()) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}