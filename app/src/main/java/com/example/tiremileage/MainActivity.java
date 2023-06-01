package com.example.tiremileage;

import android.content.Intent;
import android.os.*;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.tiremileage.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    ActivityMainBinding binding;
    NavController navController;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Repository.sharedPreferences = getPreferences(MODE_PRIVATE);
        h = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        startLogin();
                        break;
                    case 1:
                        binding = ActivityMainBinding.inflate(getLayoutInflater());
                        View view = binding.getRoot();
                        setContentView(view);
                        NavHostFragment navHostFragment = binding.mainNav.getFragment();
                        navController = navHostFragment.getNavController();
                        binding.bottomNavigationView.setOnItemSelectedListener(MainActivity.this);
                        break;
                }
            }
        };

        Repository.checkAuth(h);
        new Repository().getCars();
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
        }
        return true;
    }
}