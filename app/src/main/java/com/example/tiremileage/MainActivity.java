package com.example.tiremileage;

import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.tiremileage.databinding.ActivityMainBinding;
import com.example.tiremileage.viewa.constructor.ICallBack;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, ICallBack {


    ActivityMainBinding binding;
    NavController navController;
    String currentFragRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        NavHostFragment navHostFragment = binding.mainNav.getFragment();
        navController = navHostFragment.getNavController();
        binding.bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.constructor_navigation_item): navController.navigate(R.id.constructor);  break;
            case (R.id.tire_navigation_item): navController.navigate(R.id.tireTable); break;
        }
        return true;
    }

    @Override
    public String getString() {

        return currentFragRes;
    }
}