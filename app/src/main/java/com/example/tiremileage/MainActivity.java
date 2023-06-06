package com.example.tiremileage;

import android.content.Intent;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.tiremileage.databinding.ActivityMainBinding;
import com.example.tiremileage.repository.Repository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarMenu;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    ActivityMainBinding binding;
    NavController navController;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        NavHostFragment navHostFragment = binding.mainNav.getFragment();
        navController = navHostFragment.getNavController();
        binding.bottomNavigationView.setOnItemSelectedListener(MainActivity.this);
        setContentView(view);
    }

    public void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.constructor_navigation_item): navController.navigate(R.id.constructor);  break;
            case (R.id.tire_navigation_item): navController.navigate(R.id.tireTable); break;
            case (R.id.analytics_navigation_item): navController.navigate(R.id.analyticsFragment);  break;
            case (R.id.about_proj_navigation_item): navController.navigate(R.id.aboutProgram);  break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}